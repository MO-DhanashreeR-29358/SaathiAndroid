package com.mosl.tabapp.advancesearch

import com.mosl.mobile.domain.snapshot.AdvanceSearchData
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tableview.rowadapter.TableRow

class QuickSearchTableRow (
    @JvmField val data: AdvanceSearchData
) : TableRow {
    override fun get(column: Int): String {
        val list = data.data
        return list[column].toString()
    }
}