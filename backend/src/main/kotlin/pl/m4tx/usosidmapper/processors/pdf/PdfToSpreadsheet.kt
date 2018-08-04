package pl.m4tx.usosidmapper.processors.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import pl.m4tx.usosidmapper.spreadsheet.Spreadsheet
import technology.tabula.ObjectExtractor
import technology.tabula.Table
import java.util.*

internal class PdfToSpreadsheet {
    fun convertPdfToSpreadsheet(pdfFile: ByteArray): Spreadsheet {
        var pdfDocument: PDDocument? = null
        try {
            pdfDocument = PDDocument.load(pdfFile)
            return convertPdfToSpreadsheet(pdfDocument)
        } finally {
            pdfDocument?.close()
        }
    }

    private fun convertPdfToSpreadsheet(pdfDocument: PDDocument): Spreadsheet {
        val pageIterator = ObjectExtractor(pdfDocument).extract()
        val tables = ArrayList<Table>()

        val tableExtractor = TableExtractor()
        while (pageIterator.hasNext()) {
            val page = pageIterator.next()
            tables.addAll(tableExtractor.extractTables(page))
        }

        val spreadsheet = Spreadsheet()

        for (table in tables) {
            for (row in table.rows) {
                val spreadsheetRow = spreadsheet.appendRow()
                row.forEachIndexed { index, cell ->
                    spreadsheetRow[index].value = cell.text
                }
            }
        }

        return spreadsheet
    }
}
