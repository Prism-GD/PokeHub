package dev.prismgd.pokehub.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CatalogItemEntity::class,
        FavoriteEntity::class,
        RepositoryEntity::class,
        DownloadEntity::class,
        PublisherDraftEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PokeHubDatabase : RoomDatabase() {
    abstract fun dao(): PokeHubDao

    companion object {
        @Volatile private var INSTANCE: PokeHubDatabase? = null

        fun get(context: Context): PokeHubDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PokeHubDatabase::class.java,
                    "pokehub.db"
                ).build().also { INSTANCE = it }
            }
    }
}
