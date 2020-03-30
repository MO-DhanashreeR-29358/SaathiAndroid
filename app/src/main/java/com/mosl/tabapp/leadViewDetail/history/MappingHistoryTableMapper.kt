package com.mosl.tabapp.leadViewDetail.history

import android.content.Context
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object MappingHistoryTableMapper :
  TableRowDataMapper<MappingHistoryData, MappingHistoryTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<MappingHistoryData>): List<MappingHistoryTableRow> {
    return input.map { it.transform() }
  }

  private fun MappingHistoryData.transform(): MappingHistoryTableRow {
    return MappingHistoryTableRow(data = this)
  }
}