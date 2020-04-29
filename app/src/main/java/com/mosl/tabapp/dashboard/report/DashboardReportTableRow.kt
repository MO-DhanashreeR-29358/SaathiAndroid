package com.mosl.tabapp.dashboard.report

import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tableview.rowadapter.TableRow

class DashboardReportTableRow(
  @JvmField val data: DashboardReportData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}