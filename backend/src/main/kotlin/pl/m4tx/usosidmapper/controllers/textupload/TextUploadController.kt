package pl.m4tx.usosidmapper.controllers.textupload

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.m4tx.usosidmapper.processors.TextProcessor
import pl.m4tx.usosidmapper.usermapper.DummyUserMapper

@RestController
@RequestMapping("/text")
class TextUploadController {
    @PostMapping
    fun uploadText(@RequestBody input: TextUploadInput): TextUploadResult {
        val textProcessor = TextProcessor(DummyUserMapper())
        return TextUploadResult(textProcessor.process(input.text))
    }
}
