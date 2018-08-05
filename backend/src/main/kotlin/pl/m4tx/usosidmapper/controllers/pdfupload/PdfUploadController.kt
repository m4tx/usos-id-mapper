package pl.m4tx.usosidmapper.controllers.pdfupload

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.m4tx.usosidmapper.processors.pdf.PdfProcessor
import pl.m4tx.usosidmapper.spreadsheet.writers.CsvSpreadsheetWriter
import pl.m4tx.usosidmapper.usermapper.UserMapperService
import java.io.ByteArrayOutputStream

@RestController
@RequestMapping("/pdf")
class PdfUploadController {
    @Autowired
    private lateinit var userMapperService: UserMapperService

    @PostMapping(
            value = ["/csv"],
            produces = ["text/csv"]
    )
    @ResponseBody
    fun uploadText(@RequestParam("file") uploadFile: MultipartFile)
            : ByteArray {
        val processor = PdfProcessor(userMapperService.userMapper)
        val spreadsheet = processor.process(uploadFile.bytes)
        val os = ByteArrayOutputStream()

        CsvSpreadsheetWriter().write(spreadsheet, os)
        return os.toByteArray()
    }
}
