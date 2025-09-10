package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFAUniqueReportTableMapper  :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFAUniqueReportTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFAUniqueReportTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFAUniqueReportTableRow {
        return IndirectIFAUniqueReportTableRow(data = this)
    }
}