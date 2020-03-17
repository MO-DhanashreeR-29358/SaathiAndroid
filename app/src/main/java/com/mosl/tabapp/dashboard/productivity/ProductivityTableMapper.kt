package com.mosl.tabapp.dashboard.productivity

import android.content.Context
import com.mosl.mobile.domain.productivity.ProductivityData
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableviewx.TableRowDataMapper

object ProductivityTableMapper :
  TableRowDataMapper<ProductivityData, ProductivityTableRow> {
  override fun header(context: Context): HeaderRow {
    return DynamicHeaderRow(context)
  }

  override fun map(input: List<ProductivityData>): List<ProductivityTableRow> {
    return input.map { it.transform() }
  }

  private fun ProductivityData.transform(): ProductivityTableRow {
    return ProductivityTableRow(data = this)
  }
}