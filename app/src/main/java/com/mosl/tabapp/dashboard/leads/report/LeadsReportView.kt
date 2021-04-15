package com.mosl.tabapp.dashboard.leads.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.lead.report.LeadsReportModel
import com.mosl.tabapp.dashboard.leads.LeadUiEvent
import io.reactivex.rxjava3.core.Observable

interface LeadsReportView : BaseView {
  fun getWidgetSystemsName(): String?
  fun renderData(uiModel: UiModel<LeadsReportModel>)
  fun getAddtionalParameterMap(): Map<String, String>
  fun cellEvent(): Observable<LeadUiEvent.CellClick>
}