package com.mosl.tabapp.table

import android.view.Gravity
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableview.rowadapter.SortOrder
import com.mosl.tableviewx.SortHeaderCellFactory

class ReportHeaderCellFactory(
  headers: HeaderRow,
  sortByColumnId: Int = -1,
  sortOrder: SortOrder = SortOrder.ASCENDING
) : SortHeaderCellFactory(headers, sortByColumnId, sortOrder) {

  override fun getLayout(): Int {
    return R.layout.item_reports_grid_header
  }

  override fun bindView(
    view: View,
    row: Int,
    column: Int
  ) {
    val tv = view as ReportsSortActionTextView
    val normalizedColumn = column + 1
    header.get(normalizedColumn).let { (label, labelRes) ->
      if (label != null) {
        tv.text = label
      } else {
        tv.setText(labelRes)
      }
    }
    if (column == -1) {
      tv.gravity = Gravity.CENTER_VERTICAL or Gravity.START
    } else {
      tv.gravity = Gravity.CENTER
    }
    val (columnId, sortOrder) = sortInfo
//    tv.setSorted(columnId == normalizedColumn, sortOrder.isAscending)
  }
}