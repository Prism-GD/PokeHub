package dev.prismgd.pokehub.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RepositoryIndexTest {
    @Test fun parsesValidCatalogEntry() {
        val json = """{"schemaVersion":1,"items":[{"id":"sample-tool","name":"Sample Tool","description":"Demo","category":"tool","platform":"Android","version":"1.0.0","license":"MIT","downloadUrl":"https://example.org/tool.zip","sha256":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}]}"""
        val result = RepositoryIndex.parse(json, "community")
        assertEquals("community:sample-tool", result.single().id)
        assertEquals("tool", result.single().category)
    }

    @Test fun rejectsHttpDownloadUrls() {
        val json = """{"schemaVersion":1,"items":[{"id":"sample-tool","name":"Sample Tool","category":"tool","downloadUrl":"http://example.org/tool.zip"}]}"""
        assertThrows(IllegalArgumentException::class.java) {
            RepositoryIndex.parse(json, "community")
        }
    }

    @Test fun rejectsCommercialRomCategory() {
        val json = """{"schemaVersion":1,"items":[{"id":"rom","name":"Commercial ROM","category":"rom"}]}"""
        assertThrows(IllegalArgumentException::class.java) {
            RepositoryIndex.parse(json, "community")
        }
    }
}
