package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAMyLeadView :BaseView {
    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)
    fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>)
    fun  onDataRenderCalled() :Observable<IndirectIFAMyLeadUiEvent.onDataRenderCall>

//    fun onFilterEventCalled(): Observable<IndirectIFADashboardUIEvent.onFilterChanged>
//
//    fun fnRenderFilterEventCalled(strFilterVal:String)
    fun fngetFilterValue():String

    fun onTableRowClicked():Observable<IndirectIFAMyLeadUiEvent.onTableRowClicked>
}