package org.kottorrent.bencoding

abstract class BencodedElement {
    abstract fun encode(): String
}