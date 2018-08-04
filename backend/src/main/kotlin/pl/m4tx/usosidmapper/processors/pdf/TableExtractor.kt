package pl.m4tx.usosidmapper.processors.pdf

import technology.tabula.Page
import technology.tabula.Table
import technology.tabula.detectors.NurminenDetectionAlgorithm
import technology.tabula.extractors.BasicExtractionAlgorithm
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm
import java.util.*

internal class TableExtractor {
    private val basicExtractor = BasicExtractionAlgorithm()
    private val spreadsheetExtractor = SpreadsheetExtractionAlgorithm()
    private val tableDetector = NurminenDetectionAlgorithm()

    fun extractTables(page: Page): List<Table> {
        return if (spreadsheetExtractor.isTabular(page)) {
            extractTablesSpreadsheet(page)
        } else {
            extractTablesBasic(page)
        }
    }

    private fun extractTablesBasic(page: Page): List<Table> {
        val tables = ArrayList<Table>()

        for (guessRect in tableDetector.detect(page)) {
            val guess = page.getArea(guessRect)
            tables.addAll(basicExtractor.extract(guess))
        }

        return tables
    }

    private fun extractTablesSpreadsheet(page: Page): List<Table> {
        return spreadsheetExtractor.extract(page)
    }
}
