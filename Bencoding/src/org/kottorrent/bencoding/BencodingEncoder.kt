package org.kottorrent.bencoding

class BencodingEncoder {

    fun encode(input: Any): String {
        val element = when (input) {
            is String -> BencodedString(input)
            is Int -> BencodedInteger(input)
            is Long -> BencodedInteger(input)
            is List<*> -> BencodedList(input.toList().requireNoNulls())
            is Map<*, *> -> {
                try {
                    val castValues = input
                        .toMap()
                        .mapKeys { it.key as String }
                        .mapValues { it.value as Any }
                    BencodedDictionary(castValues)
                } catch (e: ClassCastException) {
                    throw IllegalArgumentException("Provided map could not be converted to supported types", e)
                }
            }
            else -> throw IllegalArgumentException("Specified value $input is not bencodable")
        }

        return element.encode()
    }

    fun Decode(input: String): Any {
        TODO("Not implemented yet, input was $input")
    }
}