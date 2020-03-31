package com.mosl.tabapp.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.report.HierarchyReportCombo
import io.reactivex.Observable

interface HierarchyReportView : BaseView {

  fun onNavigateEvent(): Observable<HierarchyReportUIEvent.CellEvent>

  fun getAdditionalParameterMap(): Map<String, String>

  fun renderData(uiModel: UiModel<HierarchyReportCombo>)
}