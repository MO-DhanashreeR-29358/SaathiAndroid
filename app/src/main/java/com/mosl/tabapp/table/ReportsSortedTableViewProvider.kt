package com.mosl.tabapp.table

import android.content.Context
import com.mosl.tableview.rowadapter.CellFactoryProvider
import com.mosl.tableview.rowadapter.HeaderRow
import com.mosl.tableview.rowadapter.SimpleTableDataSet
import com.mosl.tableview.rowadapter.TableDataSet
import com.mosl.tableview.rowadapter.TableRow
import com.mosl.tableview.rowadapter.TableRowAdapter
import com.mosl.tableview.rowadapter.sortDataAndSet
import com.mosl.tableviewx.FirstBodyCellFactory
import com.mosl.tableviewx.SimpleBodyCellFactory
import com.mosl.tableviewx.SortedTableViewProvider
import com.mosl.tableviewx.TableRowDataMapper

class ReportsSortedTableViewProvider(
  context: Context,
  tableDataSet: TableDataSet<*>,
  sortHeaderCellFactory: ReportHeaderCellFactory = ReportHeaderCellFactory(tableDataSet.header),
  cellFactoryProvider: CellFactoryProvider = CellFactoryProvider(
    context,
    sortHeaderCellFactory,
    listOf(
      FirstBodyCellFactory(tableDataSet),
      SimpleBodyCellFactory(tableDataSet)
    )
  )
) : SortedTableViewProvider(context, tableDataSet, sortHeaderCellFactory, cellFactoryProvider)

fun <R : TableRow> Context.createRowAdapter(
  dataMapper: TableRowDataMapper<*, R>,
  enableSorting: Boolean = true
): TableRowAdapter<R> {
  val dataSet = SimpleTableDataSet<R>(dataMapper.header(this))
  val tableViewProvider = ReportsSortedTableViewProvider(this, dataSet)
  val tableRowAdapter = TableRowAdapter(
    tableDataSet = dataSet,
    viewProvider = tableViewProvider
  )
  if (enableSorting) {
    tableViewProvider.enableSorting { pair ->
      tableRowAdapter.sortDataAndSet(dataSet.getAll(), pair)
    }
  }
  return tableRowAdapter
}

fun <R : TableRow> Context.createRowAdapter(
  headerRow: HeaderRow,
  enableSorting: Boolean = true
): TableRowAdapter<R> {
  val dataSet = SimpleTableDataSet<R>(headerRow)
  val tableViewProvider = ReportsSortedTableViewProvider(this, dataSet)
  val tableRowAdapter = TableRowAdapter(
    tableDataSet = dataSet,
    viewProvider = tableViewProvider
  )
  if (enableSorting) {
    tableViewProvider.enableSorting { pair ->
      tableRowAdapter.sortDataAndSet(dataSet.getAll(), pair)
    }
  }
  return tableRowAdapter
}