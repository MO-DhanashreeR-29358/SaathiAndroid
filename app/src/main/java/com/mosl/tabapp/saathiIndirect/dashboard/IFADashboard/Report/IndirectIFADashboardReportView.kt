package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardReportView :BaseView {
    fun getWidgetName() : String
    fun renderData(uiModel: UiModel<DashboardReportCombo>)
    fun onReportClickEvent() : Observable<IndirectIFADashboardReportUIEvent.OnReportClick>
    fun fnRenderFilterList(uiModel: UiModel<List<LookUp>>)

    fun onFilterEventCalled(): Observable<IndirectIFADashboardReportUIEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)
    fun fngetFilterValue():String

    fun onCallDataEvent():Observable<IndirectIFADashboardReportUIEvent.OnCallDataEvent>

    fun onDateFilterEventCalled():Observable<IndirectIFADashboardReportUIEvent.onDateFilterChanged>

    fun fnRenderDateFilterEventCalled(strDateFilterVal:String)
    fun  fngetDateFilterValue():String

    fun onCallDateDataEvent():Observable<IndirectIFADashboardReportUIEvent.OnCallDateDataEvent>
}