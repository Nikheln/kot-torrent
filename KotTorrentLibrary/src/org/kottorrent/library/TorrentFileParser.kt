package org.kottorrent.library

import org.kottorrent.bencoding.BencodingEncoder

class TorrentFileParser {

    internal fun parseTorrentFileContents(input: String): Torrent {
        val decoder = BencodingEncoder()
        val baseDictionary = decoder.decode(input) as? Map<*, *>
            ?: throw IllegalArgumentException("Provided torrent file was not in the correct format")
        val announceUrl = baseDictionary["announce"] as? String
            ?: throw IllegalArgumentException("No announce URL found")

        val infoDictBase = baseDictionary["info"] as? Map<*, *>
            ?: throw IllegalArgumentException("No info dictionary found")
        val infoDict = constructInfoDictionary(infoDictBase)

        return Torrent(infoDict, announceUrl)
    }

    private fun constructInfoDictionary(baseMap: Map<*, *>): InfoDictionary {
        val isSingleFileInfoDictionary = baseMap.containsKey("length")

        val pieceLength = baseMap["piece length"] as? Long
            ?: throw IllegalArgumentException("Mandatory piece length property not found from info dictionary")
        val pieces = baseMap["pieces"] as? String
            ?: throw IllegalArgumentException("Mandatory pieces property not found from info dictionary")

        if (isSingleFileInfoDictionary) {
            val fileName = baseMap["name"] as? String
                ?: throw IllegalArgumentException("Mandatory name property not found from info dictionary")
            val length = baseMap["length"] as? Long
                ?: throw IllegalArgumentException("Mandatory length property not found from info dictionary")

            return SingleFileInfoDictionary(pieceLength, pieces, fileName, length)
        } else {
            val dirName = baseMap["name"] as? String
                ?: throw IllegalArgumentException("Mandatory name property not found from info dictionary")
            val files = baseMap["files"] as? List<*>
                ?: throw IllegalArgumentException("Mandatory file list was not found")

            val fileInfos = files
                .filterIsInstance<Map<String, Any>>()
                .map {
                    val fileLength = it["length"] as? Long
                        ?: throw IllegalArgumentException("A file entry without length was found")
                    val filePath = it["path"] as? List<*>
                        ?: throw IllegalArgumentException("A file entry without path was found")

                    Pair(fileLength, filePath.filterIsInstance<String>())
                }

            return MultiFileInfoDictionary(pieceLength, pieces, dirName, fileInfos)
        }
    }
}