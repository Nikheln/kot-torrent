package org.kottorrent.bencoding

internal data class BencodedDictionary(val wrappedDictionary: Map<String, Any>) :
    BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()
        val encoder = BencodingEncoder()

        sb.append('d')
        wrappedDictionary
            .toSortedMap()
            .forEach {
                sb.append(encoder.encode(it.key))
                sb.append(encoder.encode(it.value))
            }
        sb.append('e')

        return sb.toString()
    }
}