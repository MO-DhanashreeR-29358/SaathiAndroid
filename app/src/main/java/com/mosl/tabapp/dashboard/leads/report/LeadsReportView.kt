package com.mosl.tabapp.dashboard.leads.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.lead.report.LeadsReportModel

interface LeadsReportView : BaseView {
  fun getWidgetSystemsName(): String?
  fun renderData(uiModel: UiModel<LeadsReportModel>)
  fun getAddtionalParameterMap(): Map<String, String>
}