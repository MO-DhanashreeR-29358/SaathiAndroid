package com.mosl.tabapp.leadViewDetail.history

import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryData
import com.mosl.tableview.rowadapter.TableRow

class MappingHistoryTableRow(
  @JvmField val data: MappingHistoryData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}