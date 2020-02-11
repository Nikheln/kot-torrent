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

    @Test
    fun `decoder handles strings correctly`() {
        assertEquals("hamandspam", encoder.decode("10:hamandspam"))
    }

    @Test
    fun `decoder handles integers correctly`() {
        assertEquals(257L, encoder.decode("i257e"))
    }

    @Test
    fun `decoder handles simple lists correctly`() {
        assertEquals(listOf("aa", "bbbbb", 25L), encoder.decode("l2:aa5:bbbbbi25ee"))
    }

    @Test
    fun `decoder handles simple dictionaries correctly`() {
        assertEquals(
            sortedMapOf(Pair("key1", 25L), Pair("key2", "spam"), Pair("potato", listOf(1L, 2L, 3L))),
            encoder.decode("d4:key1i25e4:key24:spam6:potatoli1ei2ei3eee")
        )
    }

    @Test
    fun `encoding-decoding test`() {
        fun test(input: Any) {
            assertEquals(input, encoder.decode(encoder.encode(input)))
        }

        test("a")
        test(25L)
        test("a long string with more than 10 characters in it")
        test((0L..1000L).toList())
        test(
            listOf(
                listOf("a", "bb", "ccc", "dddddddddd"),
                7L,
                "a string in the middle of the list",
                mapOf(Pair("first key", 0L), Pair("second key", "second value"))
            )
        )
        test("")
        test(0L)
        test(-256L)
        test(Long.MAX_VALUE / 2)
    }
}