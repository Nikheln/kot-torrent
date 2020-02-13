package org.kottorrent.library

internal data class Torrent(val info: InfoDictionary, val announceUrl: String) {}

internal abstract class InfoDictionary {
    abstract val pieceLength: Long
    abstract val pieces: String
}

internal data class SingleFileInfoDictionary(
    override val pieceLength: Long,
    override val pieces: String,
    val name: String,
    val length: Long
) : InfoDictionary() {}

internal data class MultiFileInfoDictionary(
    override val pieceLength: Long,
    override val pieces: String,
    val name: String,
    val files: List<Pair<Long, List<String>>>
) : InfoDictionary() {}