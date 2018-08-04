package pl.m4tx.usosidmapper.processors.pdf

import pl.m4tx.usosidmapper.Constants
import pl.m4tx.usosidmapper.spreadsheet.Spreadsheet
import pl.m4tx.usosidmapper.usermapper.UserMapper

class PdfProcessor(private val userMapper: UserMapper) {
    fun process(pdfFile: ByteArray): Spreadsheet {
        val spreadsheet = PdfToSpreadsheet().convertPdfToSpreadsheet(pdfFile)

        for (row in spreadsheet) {
            for (cell in row) {
                val result = Constants.SEARCH_REGEX.find(cell.value)
                if (result != null) {
                    val user = userMapper.getUserById(result.value)
                    if (user != null) {
                        row += user.firstName
                        row += user.lastName

                        break
                    }
                }
            }
        }

        return spreadsheet
    }
}
