package org.kottorrent.bencoding

internal data class BencodedList(val items: List<Any>) : BencodedElement() {

    override fun encode(): String {
        val sb = StringBuilder()

        sb.append('l')
        items.forEach {
            sb.append(create(it).encode())
        }
        sb.append('e')

        return sb.toString()
    }

}