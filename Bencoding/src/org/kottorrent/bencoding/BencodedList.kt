package org.kottorrent.bencoding

internal data class BencodedList(val items: List<BencodedElement>) : BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('l')
        items.forEach {
            sb.append(it.encode())
        }
        sb.append('e')

        return sb.toString()
    }

}