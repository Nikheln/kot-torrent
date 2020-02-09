package org.kottorrent.bencoding

abstract class BencodedElement {
    abstract fun encode(): String

    companion object Factory {

        fun create(value: Any): BencodedElement {
            return when (value) {
                is String -> BencodedString(value)
                is Int -> BencodedInteger(value)
                is Long -> BencodedInteger(value)
                is List<*> -> BencodedList(value.toList().requireNoNulls())
                is Map<*, *> -> {
                    try {
                        val castValues = value
                            .toMap()
                            .mapKeys { it.key as String }
                            .mapValues { it.value as Any }
                        return BencodedDictionary(castValues)
                    } catch (e: ClassCastException) {
                        throw IllegalArgumentException("Provided map could not be converted to supported types", e)
                    }
                }
                else -> throw IllegalArgumentException("Specified value $value is not bencodable")
            }
        }
    }
}