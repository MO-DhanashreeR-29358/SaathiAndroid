package com.mosl.tabapp.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import io.reactivex.Observable

interface ReportView : BaseView {

  fun onNavigateEvent(): Observable<CellEvent>
  fun onDatePickerEvent(): Observable<Unit>

  fun renderData(uiModel: UiModel<HierarchyReportCombo>)
  fun renderBranchFilter(uiModel: UiModel<List<AssignedLeadReportModel>>)

  fun widgetSystemsName(): String?
  fun titleName(): String?
  fun fromDate(): String
  fun toDate(): String
  fun onBranchChange(): Observable<Int>
  fun getBranch(): AssignedLeadReportModel?
}