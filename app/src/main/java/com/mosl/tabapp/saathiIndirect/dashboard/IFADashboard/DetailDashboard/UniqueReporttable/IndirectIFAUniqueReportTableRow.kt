package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable

import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.tableview.rowadapter.TableRow

class IndirectIFAUniqueReportTableRow (
    @JvmField val data: BusinessSnapshotData
) : TableRow {
    override fun get(column: Int): String {
        val list = data.data
        return list[column]
    }
}