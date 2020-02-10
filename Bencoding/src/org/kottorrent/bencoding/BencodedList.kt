package org.kottorrent.bencoding

internal data class BencodedList(val items: List<Any>) : BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()
        val encoder = BencodingEncoder()

        sb.append('l')
        items.forEach {
            sb.append(encoder.encode(it))
        }
        sb.append('e')

        return sb.toString()
    }

}