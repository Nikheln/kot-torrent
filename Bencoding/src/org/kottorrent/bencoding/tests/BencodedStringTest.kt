package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.kottorrent.bencoding.BencodedString
import kotlin.test.assertEquals

internal class BencodedStringTest {

    @Test
    fun `empty string is encoded correctly`() {
        val bencString = BencodedString("")
        assertEquals("0:", bencString.encode())
    }

    @Test
    fun `short string is encoded correctly`() {
        val bencString = BencodedString("spam")
        assertEquals("4:spam", bencString.encode())
    }

    @Test
    fun `long string is encoded correctly`() {
        val bencString = BencodedString("spam with more than 10 characters")
        assertEquals("33:spam with more than 10 characters", bencString.encode())
    }
}