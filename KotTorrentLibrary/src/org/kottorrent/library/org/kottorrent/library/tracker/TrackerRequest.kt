package org.kottorrent.library.org.kottorrent.library.tracker

internal data class TrackerRequest(
    val infoHash: String,
    val peerId: String,
    val port: Long,
    val uploadedBytes: String,
    val downloadedBytes: String,
    val leftBytes: String,
    val event: TrackerRequestEventType
)

internal enum class TrackerRequestEventType {
    STARTED,
    STOPPED,
    COMPLETED
}