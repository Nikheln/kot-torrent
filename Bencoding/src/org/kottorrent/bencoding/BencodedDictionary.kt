package org.kottorrent.bencoding

internal data class BencodedDictionary(val wrappedDictionary: Map<String, Any>) :
    BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('d')
        wrappedDictionary
            .toSortedMap()
            .forEach {
                sb.append(BencodedString(it.key).encode())
                sb.append(create(it.value).encode())
            }
        sb.append('e')

        return sb.toString()
    }
}