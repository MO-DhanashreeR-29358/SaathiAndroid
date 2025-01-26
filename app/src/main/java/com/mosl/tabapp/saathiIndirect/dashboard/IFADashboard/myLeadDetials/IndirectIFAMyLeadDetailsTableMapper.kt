package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFAMyLeadDetailsTableMapper :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFAMyLeadDetailsTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFAMyLeadDetailsTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFAMyLeadDetailsTableRow {
        return IndirectIFAMyLeadDetailsTableRow(data = this)
    }
}