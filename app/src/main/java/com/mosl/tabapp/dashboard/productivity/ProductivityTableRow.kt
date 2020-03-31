package com.mosl.tabapp.dashboard.productivity

import com.mosl.mobile.domain.productivity.ProductivityData
import com.mosl.tableview.rowadapter.TableRow

class ProductivityTableRow(
  @JvmField val data: ProductivityData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}