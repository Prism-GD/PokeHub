package dev.prismgd.pokehub.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_items")
data class CatalogItemEntity(
    @PrimaryKey val id: String,
    val repositoryId: String,
    val name: String,
    val description: String,
    val category: String,
    val platform: String,
    val version: String,
    val license: String,
    val downloadUrl: String?,
    val sha256: String?,
    val homepage: String?,
    val updatedAt: Long
)

@Entity(tableName = "favorites")
data class FavoriteEntity(@PrimaryKey val itemId: String, val addedAt: Long = System.currentTimeMillis())

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val indexUrl: String,
    val enabled: Boolean = true,
    val lastSyncedAt: Long = 0L
)

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val id: String,
    val itemId: String,
    val fileName: String,
    val status: String,
    val progress: Int = 0,
    val localPath: String? = null,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "publisher_drafts")
data class PublisherDraftEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val version: String,
    val category: String,
    val platform: String,
    val license: String,
    val downloadUrl: String,
    val sha256: String,
    val homepage: String?,
    val createdAt: Long = System.currentTimeMillis()
)
