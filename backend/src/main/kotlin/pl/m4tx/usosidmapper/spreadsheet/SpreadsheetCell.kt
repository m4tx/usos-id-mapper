package pl.m4tx.usosidmapper.spreadsheet

class SpreadsheetCell(
        private val row: SpreadsheetRow,
        val cellIndex: Int,
        var value: String = ""
) {
    val rowIndex: Int
        get() = row.rowIndex

    override fun toString(): String = value
}
