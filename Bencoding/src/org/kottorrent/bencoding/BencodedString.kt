package org.kottorrent.bencoding

internal data class BencodedString(val wrappedString: String) : BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append(wrappedString.length.toString())
        sb.append(':')
        sb.append(wrappedString)

        return sb.toString()
    }
}