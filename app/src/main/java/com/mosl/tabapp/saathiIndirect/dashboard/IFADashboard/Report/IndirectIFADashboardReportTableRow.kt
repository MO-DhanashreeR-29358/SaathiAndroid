package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tableview.rowadapter.TableRow

class IndirectIFADashboardReportTableRow (
    @JvmField val data: DashboardReportData
) : TableRow {
    override fun get(column: Int): String {
        val list = data.data
        return list[column]
    }
}