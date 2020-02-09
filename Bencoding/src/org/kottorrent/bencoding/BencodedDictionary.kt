package org.kottorrent.bencoding

internal data class BencodedDictionary(val wrappedDictionary: Map<BencodedString, BencodedElement>) :
    BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('d')
        wrappedDictionary
            .toSortedMap(Comparator { t, t2 -> t.wrappedString.compareTo(t2.wrappedString) })
            .forEach {
                sb.append(it.key.encode())
                sb.append(it.value.encode())
            }
        sb.append('e')

        return sb.toString()
    }
}