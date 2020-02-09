package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.kottorrent.bencoding.BencodedInteger
import org.kottorrent.bencoding.BencodedList
import org.kottorrent.bencoding.BencodedString
import kotlin.test.assertEquals

internal class BencodedListTest {

    @Test
    fun `empty list is encoded correctly`() {
        val bencList = BencodedList(listOf<BencodedString>())
        assertEquals("le", bencList.encode())
    }

    @Test
    fun `string list is encoded correctly`() {
        val bencList = BencodedList(listOf(BencodedString("spam"), BencodedString("maps")))
        assertEquals("l4:spam4:mapse", bencList.encode())
    }

    @Test
    fun `integer list is encoded correctly`() {
        val bencList = BencodedList(listOf(BencodedInteger(25), BencodedInteger(5733)))
        assertEquals("li25ei5733ee", bencList.encode())
    }

    @Test
    fun `mixed list is encoded correctly`() {
        val bencList = BencodedList(listOf(BencodedInteger(25), BencodedString("25")))
        assertEquals("li25e2:25e", bencList.encode())
    }

    @Test
    fun `list of lists is encoded correctly`() {
        val bencList = BencodedList(
            listOf(
                BencodedList(listOf(BencodedInteger(25), BencodedInteger(5733))),
                BencodedList(listOf(BencodedString("spam"), BencodedString("maps")))
            )
        )
        assertEquals("lli25ei5733eel4:spam4:mapsee", bencList.encode())
    }
}