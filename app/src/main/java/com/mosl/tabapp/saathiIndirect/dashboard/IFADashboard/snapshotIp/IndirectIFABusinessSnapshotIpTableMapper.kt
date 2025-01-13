package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFABusinessSnapshotIpTableMapper:
    TableRowDataMapper<BusinessSnapshotData, IndirectIFABusinessSnapshotIpTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFABusinessSnapshotIpTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFABusinessSnapshotIpTableRow {
        return IndirectIFABusinessSnapshotIpTableRow(data = this)
    }
}