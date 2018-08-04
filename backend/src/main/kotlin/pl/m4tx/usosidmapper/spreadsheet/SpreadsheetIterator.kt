package pl.m4tx.usosidmapper.spreadsheet

class SpreadsheetIterator(
        private val iterator: MutableIterator<Map.Entry<Int, SpreadsheetRow>>
) : MutableIterator<SpreadsheetRow> {
    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): SpreadsheetRow = iterator.next().value

    override fun remove() = iterator.remove()
}
