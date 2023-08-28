package com.mosl.tabapp.reactivation.detail

import android.content.Context
import com.mosl.mobile.domain.reactivation.ReactivationDetailData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object ReactivationDetailTableMapper :
  TableRowDataMapper<ReactivationDetailData, ReactivationDetailTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<ReactivationDetailData>): List<ReactivationDetailTableRow> {
    return input.map { it.transform() }
  }

  private fun ReactivationDetailData.transform(): ReactivationDetailTableRow {
    return ReactivationDetailTableRow(data = this)
  }
}