package dev.prismgd.pokehub.repository

import dev.prismgd.pokehub.data.PokeHubDao
import dev.prismgd.pokehub.data.RepositoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class RepositorySync(private val dao: PokeHubDao) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun addAndSync(id: String, name: String, indexUrl: String) = withContext(Dispatchers.IO) {
        require(id.matches(Regex("[a-zA-Z0-9._-]{2,80}"))) { "Invalid repository id" }
        require(indexUrl.startsWith("https://")) { "Repository index must use HTTPS" }
        val repository = RepositoryEntity(id, name, indexUrl)
        dao.upsertRepository(repository)
        sync(repository)
    }

    suspend fun syncEnabled(): Int = withContext(Dispatchers.IO) {
        var synced = 0
        dao.enabledRepositories().forEach { repository ->
            runCatching { sync(repository); synced++ }
        }
        synced
    }

    private suspend fun sync(repository: RepositoryEntity) = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(repository.indexUrl).get().build()
        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Repository returned HTTP ${response.code}" }
            val body = response.body?.string() ?: error("Empty repository index")
            val parsed = RepositoryIndex.parse(body, repository.id)
            dao.upsertItems(parsed)
            dao.markRepositorySynced(repository.id, System.currentTimeMillis())
        }
    }
}
