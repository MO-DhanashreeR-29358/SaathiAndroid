package com.mosl.tabapp.dashboard.report.hierarchy

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.dashboard.report.DashboardReportUIEvent.CellEvent
import io.reactivex.Observable

interface DashboardHierarchyReportView : BaseView {

  fun onNavigateEvent(): Observable<CellEvent>

  fun renderData(uiModel: UiModel<DashboardReportCombo>)
}