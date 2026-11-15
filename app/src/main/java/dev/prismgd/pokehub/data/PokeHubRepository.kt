package dev.prismgd.pokehub.data

import kotlinx.coroutines.flow.Flow

class PokeHubRepository(private val dao: PokeHubDao) {
    fun catalog(): Flow<List<CatalogItemEntity>> = dao.observeCatalog()
    fun favorites(): Flow<List<CatalogItemEntity>> = dao.observeFavorites()
    fun repositories(): Flow<List<RepositoryEntity>> = dao.observeRepositories()
    fun downloads(): Flow<List<DownloadEntity>> = dao.observeDownloads()
    fun drafts(): Flow<List<PublisherDraftEntity>> = dao.observeDrafts()

    suspend fun toggleFavorite(itemId: String) {
        if (dao.isFavorite(itemId)) dao.removeFavorite(itemId)
        else dao.addFavorite(FavoriteEntity(itemId))
    }

    suspend fun addRepository(repository: RepositoryEntity) = dao.upsertRepository(repository)
    suspend fun removeRepository(id: String) = dao.removeRepository(id)
    suspend fun item(id: String) = dao.getItem(id)
    suspend fun saveDraft(draft: PublisherDraftEntity) = dao.saveDraft(draft)
    suspend fun recordDownload(download: DownloadEntity) = dao.upsertDownload(download)
}
