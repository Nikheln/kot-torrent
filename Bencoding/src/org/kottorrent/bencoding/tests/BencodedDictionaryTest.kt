package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.kottorrent.bencoding.BencodedDictionary
import kotlin.test.assertEquals

internal class BencodedDictionaryTest {

    @Test
    fun `empty dictionary encoded correctly`() {
        val bencDict = BencodedDictionary(mapOf())

        assertEquals("de", bencDict.encode())
    }

    @Test
    fun `small dictionary encoded correctly`() {
        val bencDict = BencodedDictionary(
            mapOf(
                Pair("cow", "moo"),
                Pair("spam", "eggs")
            )
        )

        assertEquals("d3:cow3:moo4:spam4:eggse", bencDict.encode())
    }

    @Test
    fun `dictionary with list encoded correctly`() {
        val bencDict = BencodedDictionary(mapOf(Pair("spam", listOf("a", "b"))))

        assertEquals("d4:spaml1:a1:bee", bencDict.encode())
    }

    @Test
    fun `dictionary entries sorted correctly`() {
        val bencDict = BencodedDictionary(
            mapOf(
                Pair("bbb", "moo"),
                Pair("aaa", "eggs")
            )
        )

        assertEquals("d3:aaa4:eggs3:bbb3:mooe", bencDict.encode())
    }
}