package dev.prismgd.pokehub.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeHubDao {
    @Query("SELECT * FROM catalog_items ORDER BY name COLLATE NOCASE")
    fun observeCatalog(): Flow<List<CatalogItemEntity>>

    @Query("SELECT * FROM catalog_items WHERE id = :id LIMIT 1")
    suspend fun getItem(id: String): CatalogItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<CatalogItemEntity>)

    @Query("SELECT catalog_items.* FROM catalog_items INNER JOIN favorites ON favorites.itemId = catalog_items.id ORDER BY favorites.addedAt DESC")
    fun observeFavorites(): Flow<List<CatalogItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    suspend fun isFavorite(itemId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE itemId = :itemId")
    suspend fun removeFavorite(itemId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRepository(repository: RepositoryEntity)

    @Query("SELECT * FROM repositories ORDER BY name COLLATE NOCASE")
    fun observeRepositories(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repositories WHERE enabled = 1")
    suspend fun enabledRepositories(): List<RepositoryEntity>

    @Query("UPDATE repositories SET lastSyncedAt = :time WHERE id = :id")
    suspend fun markRepositorySynced(id: String, time: Long)

    @Query("DELETE FROM repositories WHERE id = :id")
    suspend fun removeRepository(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDownload(download: DownloadEntity)

    @Query("SELECT * FROM downloads ORDER BY createdAt DESC")
    fun observeDownloads(): Flow<List<DownloadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: PublisherDraftEntity)

    @Query("SELECT * FROM publisher_drafts ORDER BY createdAt DESC")
    fun observeDrafts(): Flow<List<PublisherDraftEntity>>
}
