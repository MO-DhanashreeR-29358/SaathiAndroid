package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFAMyViewTableMapper :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFAMyLeadTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFAMyLeadTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFAMyLeadTableRow {
        return IndirectIFAMyLeadTableRow(data = this)
    }
}