package pl.m4tx.usosidmapper.processors

import pl.m4tx.usosidmapper.Constants
import pl.m4tx.usosidmapper.usermapper.UserMapper

class TextProcessor(private val userMapper: UserMapper) {
    fun process(input: String): String {
        userMapper.prefetchUsers(findIds(input))
        return processInput(input)
    }

    private fun findIds(input: String): List<String> {
        val toPrefetch = ArrayList<String>()

        for (line in input.lines()) {
            val result = Constants.SEARCH_REGEX.find(line)
            if (result != null) {
                toPrefetch += result.value
            }
        }

        return toPrefetch
    }

    private fun processInput(input: String): String {
        val stringBuilder = StringBuilder()

        for (line in input.lines()) {
            val result = Constants.SEARCH_REGEX.find(line)
            var newLine = line

            if (result != null) {
                val user = userMapper.getUserById(result.value)
                if (user != null) {
                    newLine = Constants.SEARCH_REGEX.replaceFirst(
                            line, "${user.firstName} ${user.lastName}")
                }
            }

            stringBuilder.appendln(newLine)
        }

        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        return stringBuilder.toString()
    }
}
