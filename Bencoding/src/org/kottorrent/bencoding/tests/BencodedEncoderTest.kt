package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kottorrent.bencoding.BencodedElement
import kotlin.test.assertEquals

internal class BencodedEncoderTest {

    @Test
    fun `encoder throws for double values`() {
        assertThrows<IllegalArgumentException> {
            val elem = BencodedElement.create(2.5)
            elem.encode()
        }
    }

    @Test
    fun `encoder handles integers correctly`() {
        val elem = BencodedElement.create(25)
        assertEquals("i25e", elem.encode())
    }

    @Test
    fun `encoder handles lists correctly`() {
        val elem = BencodedElement.create(listOf(24, 25, 26))
        assertEquals("li24ei25ei26ee", elem.encode())
    }

    @Test
    fun `encoder handles maps correctly`() {
        val elem = BencodedElement.create(mapOf(Pair("key1", 24), Pair("key2", 25)))
        assertEquals("d4:key1i24e4:key2i25ee", elem.encode())
    }

    @Test
    fun `encoder throws for maps with wrong key type`() {
        assertThrows<IllegalArgumentException> {
            val elem = BencodedElement.create(mapOf(Pair("key1", 24), Pair(0, 25)))
            elem.encode()
        }
    }
}