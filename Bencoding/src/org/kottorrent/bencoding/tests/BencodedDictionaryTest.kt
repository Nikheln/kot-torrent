package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.kottorrent.bencoding.BencodedDictionary
import org.kottorrent.bencoding.BencodedList
import org.kottorrent.bencoding.BencodedString
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
                Pair(BencodedString("cow"), BencodedString("moo")),
                Pair(BencodedString("spam"), BencodedString("eggs"))
            )
        )

        assertEquals("d3:cow3:moo4:spam4:eggse", bencDict.encode())
    }

    @Test
    fun `dictionary with list encoded correctly`() {
        val bencDict = BencodedDictionary(
            mapOf(
                Pair(
                    BencodedString("spam"), BencodedList(
                        listOf(
                            BencodedString("a"),
                            BencodedString("b")
                        )
                    )
                )
            )
        )

        assertEquals("d4:spaml1:a1:bee", bencDict.encode())
    }
}