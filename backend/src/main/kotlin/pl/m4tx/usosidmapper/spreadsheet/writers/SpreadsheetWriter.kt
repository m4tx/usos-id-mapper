package pl.m4tx.usosidmapper.spreadsheet.writers

import pl.m4tx.usosidmapper.spreadsheet.Spreadsheet
import java.io.OutputStream

interface SpreadsheetWriter {
    fun write(spreadsheet: Spreadsheet, outputStream: OutputStream)
}
