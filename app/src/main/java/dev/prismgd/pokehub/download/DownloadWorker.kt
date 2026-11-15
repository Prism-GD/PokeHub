package dev.prismgd.pokehub.download

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.prismgd.pokehub.data.DownloadEntity
import dev.prismgd.pokehub.data.PokeHubDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest

class DownloadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val itemId = inputData.getString(KEY_ITEM_ID) ?: return@withContext Result.failure()
        val url = inputData.getString(KEY_URL) ?: return@withContext Result.failure()
        val expectedHash = inputData.getString(KEY_SHA256)?.lowercase()
        val fileName = inputData.getString(KEY_FILE_NAME) ?: "download.bin"
        val dao = PokeHubDatabase.get(applicationContext).dao()
        dao.upsertDownload(DownloadEntity(id = id.toString(), itemId = itemId, fileName = fileName, status = "running"))

        try {
            require(url.startsWith("https://")) { "Only HTTPS downloads are allowed" }
            val request = Request.Builder().url(url).build()
            OkHttpClient().newCall(request).execute().use { response ->
                check(response.isSuccessful) { "HTTP ${response.code}" }
                val targetDir = File(applicationContext.filesDir, "downloads").apply { mkdirs() }
                val target = File(targetDir, fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_"))
                val digest = MessageDigest.getInstance("SHA-256")
                response.body!!.byteStream().use { input ->
                    target.outputStream().use { output ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        while (true) {
                            if (isStopped) throw InterruptedException("Download cancelled")
                            val count = input.read(buffer)
                            if (count < 0) break
                            digest.update(buffer, 0, count)
                            output.write(buffer, 0, count)
                        }
                    }
                }
                val actualHash = digest.digest().joinToString("") { "%02x".format(it) }
                if (expectedHash != null && actualHash != expectedHash) {
                    target.delete()
                    error("SHA-256 mismatch")
                }
                dao.upsertDownload(DownloadEntity(
                    id = id.toString(), itemId = itemId, fileName = target.name,
                    status = "completed", progress = 100, localPath = target.absolutePath
                ))
            }
            Result.success()
        } catch (error: Exception) {
            dao.upsertDownload(DownloadEntity(
                id = id.toString(), itemId = itemId, fileName = fileName,
                status = "failed", error = error.message
            ))
            if (error is InterruptedException) Result.failure() else Result.retry()
        }
    }

    companion object {
        const val KEY_ITEM_ID = "item_id"
        const val KEY_URL = "url"
        const val KEY_SHA256 = "sha256"
        const val KEY_FILE_NAME = "file_name"
    }
}
