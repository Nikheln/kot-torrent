package org.kottorrent.library.tests

import org.junit.jupiter.api.Test
import org.kottorrent.library.SingleFileInfoDictionary
import org.kottorrent.library.TorrentFileParser
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class TorrentFileParserTest {

    @Test
    fun `valid torrent file parsed correctly`() {
        val sourceUri = """http://releases.ubuntu.com/19.10/ubuntu-19.10-desktop-amd64.iso.torrent"""

        val torrentString = URL(sourceUri).readText(Charsets.US_ASCII)
        assertNotNull(torrentString, "Expected torrent contents to be non-null")
        val torrentPojo = TorrentFileParser().parseTorrentFileContents(torrentString)

        assertEquals("https://torrent.ubuntu.com/announce", torrentPojo.announceUrl)
        if (torrentPojo.info is SingleFileInfoDictionary) {
            assertEquals("ubuntu-19.10-desktop-amd64.iso", torrentPojo.info.name)
        } else {
            assert(false)
        }
    }
}