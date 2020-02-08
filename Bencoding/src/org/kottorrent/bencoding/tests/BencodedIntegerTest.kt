package org.kottorrent.bencoding.tests

import org.junit.jupiter.api.Test
import org.kottorrent.bencoding.BencodedInteger
import kotlin.test.assertEquals

internal class BencodedIntegerTest {

    @Test
    fun `small int encoded correctly`() {
        val bencInt = BencodedInteger(25)

        assertEquals("i25e", bencInt.encode())
    }

    @Test
    fun `small long encoded correctly`() {
        val bencInt = BencodedInteger(25L)

        assertEquals("i25e", bencInt.encode())
    }

    @Test
    fun `large long encoded correctly`() {
        val bencInt = BencodedInteger(Long.MAX_VALUE)

        assertEquals("i9223372036854775807e", bencInt.encode())
    }

    @Test
    fun `negative int encoded correctly`() {
        val bencInt = BencodedInteger(-425)

        assertEquals("i-425e", bencInt.encode())
    }
}