package pl.m4tx.usosidmapper.spreadsheet

class SpreadsheetRowIterator(
        private val iterator: MutableIterator<Map.Entry<Int, SpreadsheetCell>>
) : MutableIterator<SpreadsheetCell> {
    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): SpreadsheetCell = iterator.next().value

    override fun remove() = iterator.remove()
}
