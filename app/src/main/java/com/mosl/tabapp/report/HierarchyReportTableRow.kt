package com.mosl.tabapp.report

import com.mosl.mobile.domain.report.HierarchyReportData
import com.mosl.tableview.rowadapter.TableRow

class HierarchyReportTableRow(
  @JvmField val data: HierarchyReportData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}