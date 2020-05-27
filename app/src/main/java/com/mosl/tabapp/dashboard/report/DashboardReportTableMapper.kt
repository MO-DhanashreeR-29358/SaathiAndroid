package com.mosl.tabapp.dashboard.report

import android.content.Context
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object DashboardReportTableMapper :
    TableRowDataMapper<DashboardReportData, DashboardReportTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<DashboardReportData>): List<DashboardReportTableRow> {
    return input.map { it.transform() }
  }

  private fun DashboardReportData.transform(): DashboardReportTableRow {
    return DashboardReportTableRow(data = this)
  }
}