package org.kottorrent.bencoding

class BencodingEncoder {

    val buffer: MutableList<Char> = mutableListOf()

    fun EncodeString(input: String) {
        input.length.toString().toCollection(buffer)
        buffer.add(':')
        input.toCollection(buffer)
    }

    fun encodeInteger(input: Int) = encodeInteger(input.toLong())

    fun encodeInteger(input: Long) {
        buffer.add('i')
        input.toString().toCollection(buffer)
        buffer.add('e')
    }


}