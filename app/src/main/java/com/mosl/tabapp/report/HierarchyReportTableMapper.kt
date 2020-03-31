package com.mosl.tabapp.report

import android.content.Context
import com.mosl.mobile.domain.report.HierarchyReportData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object HierarchyReportTableMapper :
  TableRowDataMapper<HierarchyReportData, HierarchyReportTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<HierarchyReportData>): List<HierarchyReportTableRow> {
    return input.map { it.transform() }
  }

  private fun HierarchyReportData.transform(): HierarchyReportTableRow {
    return HierarchyReportTableRow(data = this)
  }
}