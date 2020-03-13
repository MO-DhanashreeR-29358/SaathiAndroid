package com.mosl.tabapp.dashboard.snapshot

import android.content.Context
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object BusinessSnapshotTableMapper :
  TableRowDataMapper<BusinessSnapshotData, BusinessSnapshotTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<BusinessSnapshotData>): List<BusinessSnapshotTableRow> {
    return input.map { it.transform() }
  }

  private fun BusinessSnapshotData.transform(): BusinessSnapshotTableRow {
    return BusinessSnapshotTableRow(data = this)
  }
}