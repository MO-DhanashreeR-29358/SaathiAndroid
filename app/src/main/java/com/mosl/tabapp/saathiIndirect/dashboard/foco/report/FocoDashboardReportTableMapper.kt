package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import android.content.Context
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object FocoDashboardReportTableMapper :
    TableRowDataMapper<DashboardReportData, FocoDashboardReportTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<DashboardReportData>): List<FocoDashboardReportTableRow> {
    return input.map { it.transform() }
  }

  private fun DashboardReportData.transform(): FocoDashboardReportTableRow {
    return FocoDashboardReportTableRow(data = this)
  }
}