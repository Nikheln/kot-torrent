package org.kottorrent.library.tests

import org.junit.jupiter.api.Test
import org.kottorrent.library.SingleFileInfoDictionary
import org.kottorrent.library.TorrentFileParser
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class TorrentFileParserTest {

    @Test
    fun `valid torrent file parsed correctly`() {
        val sourceUri = """http://releases.ubuntu.com/19.10/ubuntu-19.10-desktop-amd64.iso.torrent"""

        val torrentString = getTorrentString(sourceUri)
        assertNotNull(torrentString, "Expected torrent contents to be non-null")
        val torrentPojo = TorrentFileParser().parseTorrentFileContents(torrentString)

        assertEquals("https://torrent.ubuntu.com/announce", torrentPojo.announceUrl)
        if (torrentPojo.info is SingleFileInfoDictionary) {
            assertEquals("ubuntu-19.10-desktop-amd64.iso", torrentPojo.info.name)
        } else {
            assert(false)
        }
    }

    private fun getTorrentString(sourceUri: String): String? {
        val stream = URL(sourceUri).openStream()
        val channel = Channels.newChannel(stream)
        val buffer = ByteBuffer.allocate(1024)

        val sb = StringBuilder()
        var readBytes = channel.read(buffer)

        while (readBytes > 0) {
            val line = String(buffer.array().sliceArray(0 until readBytes), Charsets.US_ASCII)
            sb.append(line)

            buffer.clear()
            readBytes = channel.read(buffer)
        }

        channel.close()
        stream.close()

        return sb.toString()
    }
}