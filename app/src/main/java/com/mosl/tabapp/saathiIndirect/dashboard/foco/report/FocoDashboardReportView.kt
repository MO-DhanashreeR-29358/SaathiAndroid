package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.dashboard.report.DashboardReportUIEvent.CellEvent
import com.mosl.tabapp.model.TypeActionableFilterModel
import io.reactivex.rxjava3.core.Observable

interface FocoDashboardReportView : BaseView {

    fun renderPeriodFilter(uiModel: UiModel<List<FocoPeriodModel>>)
    fun onPeriodChange(): Observable<Int>
    fun getPeriod(): FocoPeriodModel?

    fun onClientReactivationFilterChange(): Observable<Int>
    fun getClientReactivationFilter(): String?
    fun onUserTypeChange(): Observable<Int>
    fun getUserType(): String?

    fun getWidgetName() : String
    fun getEmployeeCode() : String
    fun renderData(uiModel: UiModel<DashboardReportCombo>)
    fun onReportClickEvent() : Observable<FocoDashboardReportUIEvent.OnReportClick>
}