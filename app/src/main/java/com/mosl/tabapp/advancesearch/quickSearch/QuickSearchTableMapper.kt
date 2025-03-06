package com.mosl.tabapp.advancesearch.quickSearch

import android.content.Context
import com.mosl.mobile.domain.snapshot.AdvanceSearchData
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object QuickSearchTableMapper :
    TableRowDataMapper<AdvanceSearchData, QuickSearchTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<AdvanceSearchData>): List<QuickSearchTableRow> {
        return input.map { it.transform() }
    }

    private fun AdvanceSearchData.transform(): QuickSearchTableRow {
        return QuickSearchTableRow(data = this)
    }
}