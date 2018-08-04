package pl.m4tx.usosidmapper.processors

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.m4tx.usosidmapper.usermapper.User
import pl.m4tx.usosidmapper.usermapper.UserMapper

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TextProcessorTest {
    companion object {
        private val TEST_USER_1 = User("Lorne", "Malvo")
        private val TEST_USER_2 = User("Bernard", "Lowe")
    }

    private val userMapperMock: UserMapper = mockk()
    private val processor = TextProcessor(userMapperMock)

    @Test
    @DisplayName("returns empty result for empty input")
    fun processEmpty() {
        assertThat(processor.process("")).isEmpty()
    }

    @Test
    @DisplayName("maps a single value")
    fun processSingle() {
        every { userMapperMock.getUserById("1234567") } returns TEST_USER_1
        assertThat(processor.process("test 1234567 suffix"))
                .isEqualTo("test Lorne Malvo suffix")
    }

    @Test
    @DisplayName("doesn't map an unknown value")
    fun processSingleUnknown() {
        every { userMapperMock.getUserById("1234568") } returns null
        val text = "test 1234568 suffix"
        assertThat(processor.process(text)).isEqualTo(text)
    }

    @Test
    @DisplayName("maps multiple values")
    fun processMultiple() {
        every { userMapperMock.getUserById(any()) } returns null
        every { userMapperMock.getUserById("1234567") } returns TEST_USER_1
        every { userMapperMock.getUserById("1111111") } returns TEST_USER_2

        val text = """
            test 1234567 suffix
            hi 1234568 test


            testing 1111111
            finally: 1111112, stopping
        """.trimIndent()
        val expected = """
            test Lorne Malvo suffix
            hi 1234568 test


            testing Bernard Lowe
            finally: 1111112, stopping
        """.trimIndent()

        assertThat(processor.process(text)).isEqualTo(expected)
    }
}
