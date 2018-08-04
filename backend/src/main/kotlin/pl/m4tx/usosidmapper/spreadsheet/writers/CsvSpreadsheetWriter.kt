package pl.m4tx.usosidmapper.spreadsheet.writers

import com.opencsv.CSVWriter
import pl.m4tx.usosidmapper.spreadsheet.Spreadsheet
import pl.m4tx.usosidmapper.spreadsheet.SpreadsheetRow
import java.io.OutputStream
import kotlin.streams.toList

class CsvSpreadsheetWriter : SpreadsheetWriter {
    override fun write(spreadsheet: Spreadsheet, outputStream: OutputStream) {
        val osWriter = outputStream.writer()
        osWriter.use {
            val csvWriter = CSVWriter(osWriter)
            spreadsheet.forEach { row ->
                csvWriter.writeNext(rowToList(row))
            }
        }
    }

    private fun rowToList(row: SpreadsheetRow): Array<String> =
            row.stream().map { t -> t.value }.toList().toTypedArray()
}
