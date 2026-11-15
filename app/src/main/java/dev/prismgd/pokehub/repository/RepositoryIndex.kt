package dev.prismgd.pokehub.repository

import dev.prismgd.pokehub.data.CatalogItemEntity
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository index schema v1:
 * { "schemaVersion": 1, "repository": {"id":"...","name":"..."},
 *   "items": [{"id":"...","name":"...","description":"...","category":"homebrew|patch|tool|emulator",
 *   "platform":"...","version":"...","license":"...","downloadUrl":"https://...","sha256":"64 hex chars"}] }
 */
object RepositoryIndex {
    fun parse(json: String, repositoryId: String): List<CatalogItemEntity> {
        val root = JSONObject(json)
        require(root.optInt("schemaVersion") == 1) { "Unsupported repository schema version" }
        val items = root.optJSONArray("items") ?: JSONArray()
        return buildList {
            for (index in 0 until items.length()) {
                val item = items.getJSONObject(index)
                val id = item.getString("id").trim()
                val name = item.getString("name").trim()
                val category = item.getString("category").trim().lowercase()
                require(id.matches(Regex("[a-zA-Z0-9._-]{2,100}"))) { "Invalid item id" }
                require(name.isNotBlank() && name.length <= 120) { "Invalid item name" }
                require(category in setOf("homebrew", "patch", "tool", "emulator")) { "Unsupported item category" }
                val url = item.optString("downloadUrl").takeIf { it.isNotBlank() }
                if (url != null) require(url.startsWith("https://")) { "Downloads must use HTTPS" }
                val hash = item.optString("sha256").takeIf { it.isNotBlank() }
                if (hash != null) require(hash.matches(Regex("[a-fA-F0-9]{64}"))) { "Invalid SHA-256" }
                add(
                    CatalogItemEntity(
                        id = "$repositoryId:$id",
                        repositoryId = repositoryId,
                        name = name,
                        description = item.optString("description"),
                        category = category,
                        platform = item.optString("platform"),
                        version = item.optString("version"),
                        license = item.optString("license"),
                        downloadUrl = url,
                        sha256 = hash,
                        homepage = item.optString("homepage").takeIf { it.startsWith("https://") },
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
