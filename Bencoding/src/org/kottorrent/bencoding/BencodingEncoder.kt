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

    fun decode(input: String): Any {
        return DecoderInstance().decode(input)
    }

    private class DecoderInstance {
        var currentInput: String = ""
        var currentIndex: Int = 0

        fun decode(input: String): Any {
            if (input.isEmpty()) throw IllegalArgumentException("Empty values are unsupported")

            currentInput = input
            currentIndex = 0

            val result = decodeNextElement()

            if (currentIndex != currentInput.length) throw IllegalArgumentException(
                "The specified input was not parsed correctly, index pointer did not reach the end of the string"
            )

            return result
        }

        private fun decodeNextElement(): Any {
            return when (currentInput[currentIndex]) {
                'i' -> decodeInteger()
                'l' -> decodeList()
                'd' -> decodeDictionary()
                else -> decodeString()
            }
        }

        private fun decodeInteger(): Long {
            if (currentInput[currentIndex] != 'i') throw IllegalStateException(
                "Tried to decode an integer that was not encoded with the correct prefix"
            )

            val upperLimit = currentInput.indexOf('e', currentIndex, false)

            if (upperLimit == -1) throw IllegalArgumentException("Invalid input format, an integer was not terminated with the expected suffix")

            val castedValue = currentInput
                .substring(currentIndex + 1 until upperLimit)
                .toLongOrNull(10)
                ?: throw IllegalArgumentException("Invalid input format, an integer contained non-digits")

            currentIndex = upperLimit + 1
            return castedValue
        }

        private fun decodeString(): String {
            val matcher = Regex("(\\d+):.*")
            val formatMatchResult = matcher.find(currentInput, currentIndex)
                ?: throw IllegalArgumentException("Invalid input format, a string was not correctly formatted")

            // The regex already makes sure the first capture group contains only digits and it can be safely cast
            val stringLengthStr = formatMatchResult.groupValues[1]
            val stringLength = stringLengthStr.toInt()
            val stringStartIndex = currentIndex + stringLengthStr.length + 1

            // The potential index after this parsing may reach currentInput.length, i.e. outside the index range
            // if the string reaches the end of input
            if (stringStartIndex + stringLength > currentInput.length) throw IllegalArgumentException(
                "Invalid input format, a string has been specified to exceed the input's length"
            )

            currentIndex = stringStartIndex + stringLength

            return currentInput.substring(stringStartIndex until currentIndex)
        }

        private fun decodeList(): List<Any> {
            if (currentInput[currentIndex] != 'l') throw IllegalStateException(
                "Tried to decode a list that was not encoded with the correct prefix"
            )
            val result = mutableListOf<Any>()
            currentIndex++

            while (currentIndex < currentInput.length && currentInput[currentIndex] != 'e') {
                result.add(decodeNextElement())
            }
            // After the loop terminates, currentIndex is either outside the input range (invalid input)
            // or at the 'e' terminating the list

            if (currentIndex >= currentInput.length) throw IllegalArgumentException(
                "Invalid input format, a list was not terminated properly"
            )

            // Shift the current index forwards to move after the list
            currentIndex++
            return result
        }

        private fun decodeDictionary(): Map<String, Any> {
            if (currentInput[currentIndex] != 'd') throw IllegalStateException(
                "Tried to decode a dictionary that was not encoded with the correct prefix"
            )

            currentIndex++

            val result = mutableMapOf<String, Any>()
            var previousKey: String? = null

            while (currentIndex < currentInput.length && currentInput[currentIndex] != 'e') {
                val nextKey = decodeNextElement()
                if (nextKey !is String) {
                    throw IllegalArgumentException(
                        "Invalid input format, a dictionary can only have strings as keys, got $nextKey, a ${nextKey.javaClass.kotlin.qualifiedName} instead"
                    )
                }

                // According to Bencoding specification, dictionary keys must be ordered
                if (previousKey != null && previousKey > nextKey) throw IllegalArgumentException(
                    "Invalid input format, bencoded dictionaries must have their keys in alphabetical order"
                )

                val nextValue = decodeNextElement()
                result[nextKey] = nextValue
                previousKey = nextKey
            }
            // After the loop terminates, currentIndex is either outside the input range (invalid input)
            // or at the 'e' terminating the list

            if (currentIndex >= currentInput.length) throw IllegalArgumentException(
                "Invalid input format, a dictionary was not terminated properly"
            )

            // Shift the current index forwards to move after the list
            currentIndex++
            return result
        }
    }

}