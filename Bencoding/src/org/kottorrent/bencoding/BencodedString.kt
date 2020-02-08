package org.kottorrent.bencoding

import kotlin.text.StringBuilder

class BencodedString constructor(wrappedString: String) : BencodedElement() {
    private val input = wrappedString

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append(input.length.toString())
        sb.append(':')
        sb.append(input)

        return sb.toString()
    }
}