package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFABusinessSnapshotTableMapper :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFABusinessSnapshotTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFABusinessSnapshotTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFABusinessSnapshotTableRow {
        return IndirectIFABusinessSnapshotTableRow(data = this)
    }
}