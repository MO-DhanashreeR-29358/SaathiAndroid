package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardReportView :BaseView {
    fun getWidgetName() : String
    fun renderData(uiModel: UiModel<DashboardReportCombo>)
    fun onReportClickEvent() : Observable<IndirectIFADashboardReportUIEvent.OnReportClick>
}