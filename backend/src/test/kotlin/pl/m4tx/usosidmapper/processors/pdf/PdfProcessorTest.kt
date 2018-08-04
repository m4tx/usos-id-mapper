package pl.m4tx.usosidmapper.processors.pdf

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import pl.m4tx.usosidmapper.usermapper.DummyUserMapper

internal class PdfProcessorTest {

    @Test
    fun process() {
        PdfProcessor(DummyUserMapper()).process()
    }
}
