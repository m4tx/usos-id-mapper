package pl.m4tx.usosidmapper.processors

import pl.m4tx.usosidmapper.Constants
import pl.m4tx.usosidmapper.usermapper.UserMapper

class TextProcessor(private val userMapper: UserMapper) {
    fun process(input: String): String {
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
