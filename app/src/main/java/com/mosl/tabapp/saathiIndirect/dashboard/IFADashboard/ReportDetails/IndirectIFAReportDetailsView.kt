package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.ReportDetails

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFAMyLeadDetailsFilter
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAReportDetailsView :BaseView {
    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)
    fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>)
    fun  onDataRenderCalled() : Observable<IndirectIFAReportDetailsUiEvent.onDataRenderCall>

    fun onLeadNotUpdateDataRenderCalled():Observable<IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall>
    fun fnGetFilterData(): IndirectIFAMyLeadDetailsFilter
}