package com.mosl.tabapp.table

import android.content.Context
import com.mosl.tableview.rowadapter.Header
import com.mosl.tableview.rowadapter.HeaderRow
import kotlin.math.roundToInt

class DynamicHeaderRow(
  private val context: Context
) : HeaderRow {
  private val headers: MutableList<Header> = mutableListOf(Header.EmptyHeader)
  private val widths: MutableList<Int> = mutableListOf(10)

  fun setHeader(header: List<String>) {
    headers.clear()
    headers.addAll(header.toTableHeader())
    widths.clear()
    val density = context.resources.displayMetrics.density
    widths.addAll(header.map { (140 * density).roundToInt() })
  }

  override fun width(column: Int): Int {
    return if (column < widths.size - 1) {
      widths[column + 1]
    } else {
      0
    }
  }

  override fun get(column: Int): Header {
    return headers.getOrNull(column) ?: Header.EmptyHeader
  }

  override val size: Int
    get() = headers.size

  fun List<String>.toTableHeader(): List<Header> {
    return map { Header(label = it) }
  }
}