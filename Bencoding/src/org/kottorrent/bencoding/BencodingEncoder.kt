package org.kottorrent.bencoding

class BencodingEncoder {

    fun Encode(input: Any): String {
        return BencodedElement.create(input).encode()
    }

    fun Decode(input: String): Any {
        TODO("Not implemented yet, input was $input")
    }
}