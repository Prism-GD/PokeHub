package dev.prismgd.pokehub.publisher

import dev.prismgd.pokehub.data.PublisherDraftEntity
import org.junit.Assert.assertThrows
import org.junit.Test

class PublisherManifestTest {
    private fun draft(url: String) = PublisherDraftEntity(
        id = "demo-tool", name = "Demo Tool", description = "A legal homebrew utility",
        version = "1.0.0", category = "tool", platform = "Android",
        license = "MIT", downloadUrl = url,
        sha256 = "b".repeat(64), homepage = null
    )

    @Test fun rejectsNonHttpsDownloads() {
        assertThrows(IllegalArgumentException::class.java) {
            PublisherManifest.validate(draft("http://example.org/file.zip"))
        }
    }

    @Test fun acceptsHttpsDownloadsAndHash() {
        PublisherManifest.validate(draft("https://example.org/file.zip"))
    }
}
