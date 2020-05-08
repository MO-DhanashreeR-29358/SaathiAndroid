package com.mosl.tabapp.report.hierarchy

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import io.reactivex.Observable

interface HierarchyReportView : BaseView {

  fun onNavigateEvent(): Observable<CellEvent>
  fun titleName(): String

  fun getAdditionalParameterMap(): HashMap<String, String>
  fun onViewChange(): Observable<Unit>
  fun renderData(uiModel: UiModel<HierarchyReportCombo>)
}