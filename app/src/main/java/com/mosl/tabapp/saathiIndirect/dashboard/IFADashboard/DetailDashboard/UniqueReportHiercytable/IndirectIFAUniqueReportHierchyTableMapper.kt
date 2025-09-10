package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFAUniqueReportHierchyTableMapper  :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFAUniqueReportHierchyTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFAUniqueReportHierchyTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFAUniqueReportHierchyTableRow {
        return IndirectIFAUniqueReportHierchyTableRow(data = this)
    }
}