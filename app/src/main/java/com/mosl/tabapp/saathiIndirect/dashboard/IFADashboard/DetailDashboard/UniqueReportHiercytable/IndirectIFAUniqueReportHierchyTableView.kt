package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAUniqueReportHierchyTableView :BaseView {
    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)

    fun onFilterEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.onFilterChanged>
    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onFilterCategoryEventCalled():Observable<IndirectIFADashboardDetailsUiEvent.onFilterCategoryChanged>

    fun fnRenderFilterCategoryEventCalled(strFilterCatVal:String)

    fun fngetFilterValue():String

    fun fngetFilterProductValue():String

    fun fnonMeetingEventCalled():Observable<IndirectIFAUniqueReportHierchyUiEvent.onMeetingEventCalled>

    fun fnonIfaActivationEventCalled() :Observable<IndirectIFAUniqueReportHierchyUiEvent.onIfaActivationEventCalled>

    fun fnonRevenuIpEventCalled():Observable<IndirectIFAUniqueReportHierchyUiEvent.onRevenuIpEventCalled>

    fun fnonRevenuBroEventCalled():Observable<IndirectIFAUniqueReportHierchyUiEvent.onRevenuBroEventCalled>
}