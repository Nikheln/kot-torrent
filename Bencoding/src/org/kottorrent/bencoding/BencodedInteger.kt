package org.kottorrent.bencoding

class BencodedInteger constructor(wrappedLong: Long) : BencodedElement() {
    private val input = wrappedLong

    constructor(wrappedInteger: Int) : this(wrappedInteger.toLong())

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('i')
        sb.append(input.toString(10))
        sb.append('e')

        return sb.toString()
    }
}