package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kottorrent.bencoding.BencodingEncoder
import kotlin.test.assertEquals

internal class BencodedEncoderTest {

    private val encoder = BencodingEncoder()

    @Test
    fun `encoder throws for double values`() {
        assertThrows<IllegalArgumentException> {
            encoder.encode(2.5)
        }
    }

    @Test
    fun `encoder handles integers correctly`() {
        assertEquals("i25e", encoder.encode(25))
    }

    @Test
    fun `encoder handles lists correctly`() {
        assertEquals("li24ei25ei26ee", encoder.encode(listOf(24, 25, 26)))
    }

    @Test
    fun `encoder handles maps correctly`() {
        assertEquals("d4:key1i24e4:key2i25ee", encoder.encode(mapOf(Pair("key1", 24), Pair("key2", 25))))
    }

    @Test
    fun `encoder throws for maps with wrong key type`() {
        assertThrows<IllegalArgumentException> {
            encoder.encode(mapOf(Pair("key1", 24), Pair(0, 25)))
        }
    }
}