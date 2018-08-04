package pl.m4tx.usosidmapper.spreadsheet

import java.util.*

class Spreadsheet : AbstractCollection<SpreadsheetRow>() {
    private val rows = TreeMap<Int, SpreadsheetRow>()

    operator fun get(index: Int): SpreadsheetRow {
        if (index !in rows) {
            rows[index] = SpreadsheetRow(index)
        }
        return rows[index]!!
    }

    override fun iterator(): MutableIterator<SpreadsheetRow> =
            SpreadsheetIterator(rows.iterator())

    override val size: Int
        get() = rows.size

    val firstRowIndex: Int
        get() {
            return if (rows.isEmpty()) {
                0
            } else {
                rows.firstKey()
            }
        }

    val lastRowIndex: Int
        get() {
            return if (rows.isEmpty()) {
                0
            } else {
                rows.lastKey()
            }
        }

    fun appendRow(): SpreadsheetRow {
        val rowIndex = lastRowIndex + 1
        val row = SpreadsheetRow(rowIndex)

        rows[rowIndex] = row
        return row
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        for (row in this) {
            stringBuilder.appendln(row.toString())
        }

        return stringBuilder.toString()
    }
}
