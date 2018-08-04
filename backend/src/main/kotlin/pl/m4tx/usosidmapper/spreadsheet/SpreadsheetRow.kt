package pl.m4tx.usosidmapper.spreadsheet

import java.util.*
import java.util.stream.Collectors

class SpreadsheetRow(
        val rowIndex: Int
) : AbstractCollection<SpreadsheetCell>() {
    private val cells = TreeMap<Int, SpreadsheetCell>()

    operator fun get(index: Int): SpreadsheetCell {
        if (index !in cells) {
            cells[index] = SpreadsheetCell(this, index)
        }
        return cells[index]!!
    }

    override fun iterator(): MutableIterator<SpreadsheetCell> =
            SpreadsheetRowIterator(cells.iterator())

    override val size: Int
        get() = cells.size

    val firstCellIndex: Int
        get() {
            return if (cells.isEmpty()) {
                0
            } else {
                cells.firstKey()
            }
        }

    val lastCellIndex: Int
        get() {
            return if (cells.isEmpty()) {
                0
            } else {
                cells.lastKey()
            }
        }

    operator fun plusAssign(cellValue: String) {
        this[lastCellIndex + 1].value = cellValue
    }

    override fun toString(): String {
        val cellsString = cells.values.stream()
                .map(SpreadsheetCell::toString)
                .collect(Collectors.joining(" | "))
        return "${rowIndex.toString().padStart(3)} || $cellsString"
    }
}
