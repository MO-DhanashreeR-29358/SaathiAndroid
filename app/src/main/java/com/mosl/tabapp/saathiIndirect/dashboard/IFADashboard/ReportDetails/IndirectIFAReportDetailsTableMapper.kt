package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.ReportDetails

import android.content.Context
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFAReportDetailsTableMapper  :
    TableRowDataMapper<BusinessSnapshotData, IndirectIFAReportDetailsTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<BusinessSnapshotData>): List<IndirectIFAReportDetailsTableRow> {
        return input.map { it.transform() }
    }

    private fun BusinessSnapshotData.transform(): IndirectIFAReportDetailsTableRow {
        return IndirectIFAReportDetailsTableRow(data = this)
    }
}