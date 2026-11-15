package dev.prismgd.pokehub.publisher

import dev.prismgd.pokehub.data.PublisherDraftEntity
import org.json.JSONArray
import org.json.JSONObject

object PublisherManifest {
    fun validate(draft: PublisherDraftEntity) {
        require(draft.name.isNotBlank() && draft.name.length <= 120) { "Name is required (max 120 chars)" }
        require(draft.description.isNotBlank() && draft.description.length <= 2000) { "Description is required (max 2000 chars)" }
        require(draft.version.matches(Regex("[0-9]+(\\.[0-9A-Za-z-]+){1,3}"))) { "Use a version such as 1.0.0" }
        require(draft.category in setOf("homebrew", "patch", "tool", "emulator")) { "Unsupported category" }
        require(draft.license.isNotBlank()) { "License is required" }
        require(draft.downloadUrl.startsWith("https://")) { "Download URL must use HTTPS" }
        require(draft.sha256.matches(Regex("[a-fA-F0-9]{64}"))) { "A valid SHA-256 is required" }
        require(draft.homepage == null || draft.homepage.startsWith("https://")) { "Homepage must use HTTPS" }
    }

    fun toRepositoryIndex(drafts: List<PublisherDraftEntity>, repositoryId: String, repositoryName: String): String {
        require(repositoryId.matches(Regex("[a-zA-Z0-9._-]{2,80}")))
        val items = JSONArray()
        drafts.forEach { draft ->
            validate(draft)
            items.put(JSONObject().apply {
                put("id", draft.id)
                put("name", draft.name)
                put("description", draft.description)
                put("category", draft.category)
                put("platform", draft.platform)
                put("version", draft.version)
                put("license", draft.license)
                put("downloadUrl", draft.downloadUrl)
                put("sha256", draft.sha256.lowercase())
                draft.homepage?.let { put("homepage", it) }
            })
        }
        return JSONObject().apply {
            put("schemaVersion", 1)
            put("repository", JSONObject().put("id", repositoryId).put("name", repositoryName))
            put("items", items)
        }.toString(2)
    }
}
