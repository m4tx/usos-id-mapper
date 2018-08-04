package pl.m4tx.usosidmapper.controllers.pdfupload

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.m4tx.usosidmapper.processors.pdf.PdfProcessor
import pl.m4tx.usosidmapper.spreadsheet.writers.CsvSpreadsheetWriter
import pl.m4tx.usosidmapper.usermapper.DummyUserMapper
import java.io.ByteArrayOutputStream

@RestController
@RequestMapping("/pdf")
class PdfUploadController {
    @PostMapping(
            value = ["/csv"],
            produces = ["text/csv"]
    )
    @ResponseBody
    fun uploadText(@RequestParam("file") uploadFile: MultipartFile): ByteArray {
        val processor = PdfProcessor(DummyUserMapper())
        val spreadsheet = processor.process(uploadFile.bytes)
        val os = ByteArrayOutputStream()

        CsvSpreadsheetWriter().write(spreadsheet, os)
        return os.toByteArray()
    }
}
