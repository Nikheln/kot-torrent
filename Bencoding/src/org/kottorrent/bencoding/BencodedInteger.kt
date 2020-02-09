package org.kottorrent.bencoding

internal data class BencodedInteger(val wrappedLong: Long) : BencodedElement() {

    constructor(wrappedInteger: Int) : this(wrappedInteger.toLong())

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('i')
        sb.append(wrappedLong.toString(10))
        sb.append('e')

        return sb.toString()
    }
}