package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tableview.rowadapter.TableRow

class FocoDashboardReportTableRow(
  @JvmField val data: DashboardReportData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}