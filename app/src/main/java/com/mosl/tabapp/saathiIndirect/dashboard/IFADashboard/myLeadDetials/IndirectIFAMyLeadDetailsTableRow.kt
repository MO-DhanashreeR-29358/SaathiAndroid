package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials

import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tableview.rowadapter.TableRow

class IndirectIFAMyLeadDetailsTableRow (
    @JvmField val data: BusinessSnapshotData
) : TableRow {
    override fun get(column: Int): String {
        val list = data.data
        return list[column]
    }
}