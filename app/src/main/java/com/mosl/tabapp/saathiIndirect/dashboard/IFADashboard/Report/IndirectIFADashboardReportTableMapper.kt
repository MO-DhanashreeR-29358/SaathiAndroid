package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import android.content.Context
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object IndirectIFADashboardReportTableMapper :
    TableRowDataMapper<DashboardReportData, IndirectIFADashboardReportTableRow> {
    override fun header(context: Context): HeaderRow {
        return DynamicHeaderRow(context)
    }

    override fun map(input: List<DashboardReportData>): List<IndirectIFADashboardReportTableRow> {
        return input.map { it.transform() }
    }

    private fun DashboardReportData.transform(): IndirectIFADashboardReportTableRow {
        return IndirectIFADashboardReportTableRow(data = this)
    }
}