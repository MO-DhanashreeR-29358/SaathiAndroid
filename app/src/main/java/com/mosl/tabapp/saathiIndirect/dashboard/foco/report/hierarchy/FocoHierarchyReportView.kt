package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.dashboard.report.DashboardReportUIEvent.CellEvent
import com.mosl.tabapp.model.TypeActionableFilterModel
import io.reactivex.rxjava3.core.Observable

interface FocoHierarchyReportView : BaseView {

    fun getWidgetName() : String
    fun renderData(uiModel: UiModel<DashboardReportCombo>)
    fun onReportClickEvent() : Observable<FocoHierarchyReportUIEvent.OnReportClick>
}