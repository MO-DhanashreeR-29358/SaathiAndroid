package com.mosl.tabapp.reactivation.detail

import com.mosl.mobile.domain.productivity.ProductivityData
import com.mosl.mobile.domain.reactivation.ReactivationDetailData
import com.mosl.tableview.rowadapter.TableRow

class ReactivationDetailTableRow(
  @JvmField val data: ReactivationDetailData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}