package org.kottorrent.library.org.kottorrent.library.tracker

import org.kottorrent.bencoding.BencodingEncoder

internal abstract class TrackerResponse(val requestSuccessful: Boolean) {
    companion object Factory {
        fun create(responseBody: String): TrackerResponse {

            val response = BencodingEncoder().decode(responseBody)
            if (response !is Map<*, *>) {
                throw IllegalArgumentException("Response format was invalid, expected a Bencoded dictionary")
            }
            val failureReason = response["failure reason"]
            if (failureReason is String && failureReason.isNotEmpty()) {
                return FailedTrackerResponse(failureReason)
            }

            val intervalSeconds = response["interval"]
            if (intervalSeconds !is Long) {
                throw IllegalStateException("Response did not contain a valid interval field")
            }

            val peers = response["peers"]
            if (peers !is List<*>) {
                throw IllegalStateException("Response did not contain a valid peer list")
            }

            val peerList = peers.map { peerInfo ->
                val castInfo = peerInfo as? Map<*, *>
                    ?: throw IllegalStateException("Response contained an invalid peer entry")
                PeerInfo.create(castInfo)
            }.toList()

            return SuccessfulTrackerResponse(
                intervalSeconds,
                null,
                0,
                0,
                peerList
            )
        }
    }
}

internal data class SuccessfulTrackerResponse(
    val intervalSeconds: Long,
    val trackerId: String?,
    val complete: Long,
    val incomplete: Long,
    val peers: List<PeerInfo>
) : TrackerResponse(true)

internal data class FailedTrackerResponse(val failureReason: String) : TrackerResponse(false)

internal data class PeerInfo(
    val id: String,
    val ip: String,
    val port: Long
) {
    companion object Factory {
        fun create(input: Map<*, *>): PeerInfo {
            val peerId = input["peer id"]
            val peerIp = input["ip"]
            val peerPort = input["port"]
            if (peerId !is String) {
                throw IllegalStateException("Invalid or missing peer ID")
            }
            if (peerIp !is String) {
                throw IllegalStateException("Invalid or missing peer IP")
            }
            if (peerPort !is Long) {
                throw IllegalStateException("Invalid or missing peer port")
            }
            return PeerInfo(peerId, peerIp, peerPort)
        }
    }
}