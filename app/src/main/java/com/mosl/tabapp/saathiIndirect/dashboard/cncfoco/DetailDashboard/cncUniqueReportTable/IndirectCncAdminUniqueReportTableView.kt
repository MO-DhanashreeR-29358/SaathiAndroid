package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.IndirectCncFocoDashboardDetailsUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectCncAdminUniqueReportTableView : BaseView {
    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)

    fun onFilterEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterChanged>
    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onFilterCategoryEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterCategoryChanged>

    fun fnRenderFilterCategoryEventCalled(strFilterCatVal:String)
    fun fngetFilterValue():String
    fun fngetFilterProductValue():String

    fun fnonMeetingEventCalled(): Observable<IndirectCncAdminUniqueReportTableUiEvent.onMeetingEventCalled>
    fun fnonRevenuBroEventCalled(): Observable<IndirectCncAdminUniqueReportTableUiEvent.onRevenuBroEventCalled>

   // fun fnOnReportRowClicked(): Observable<IndirectIFAUniqueTableReportUiEvent.onReportRowClicked>
}