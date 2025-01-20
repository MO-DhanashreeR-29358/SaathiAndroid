package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAUniqueReportTableView :BaseView {
    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)

    fun onFilterEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)
    fun fngetFilterValue():String
}