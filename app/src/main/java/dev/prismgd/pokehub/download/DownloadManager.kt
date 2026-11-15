package dev.prismgd.pokehub.download

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.UUID

class PokeHubDownloadManager(context: Context) {
    private val workManager = WorkManager.getInstance(context.applicationContext)

    fun enqueue(itemId: String, url: String, fileName: String, sha256: String?): UUID {
        require(url.startsWith("https://")) { "Only HTTPS downloads are supported" }
        val data = Data.Builder()
            .putString(DownloadWorker.KEY_ITEM_ID, itemId)
            .putString(DownloadWorker.KEY_URL, url)
            .putString(DownloadWorker.KEY_FILE_NAME, fileName)
            .putString(DownloadWorker.KEY_SHA256, sha256)
            .build()
        val request = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(data)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        workManager.enqueue(request)
        return request.id
    }

    fun cancel(id: UUID) = workManager.cancelWorkById(id)
}
