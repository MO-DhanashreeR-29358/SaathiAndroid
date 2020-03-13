package com.mosl.tabapp.dashboard.snapshot

import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tableview.rowadapter.TableRow

class BusinessSnapshotTableRow(
  @JvmField val data: BusinessSnapshotData
) : TableRow {
  override fun get(column: Int): String {
    val list = data.data
    return list[column]
  }
}