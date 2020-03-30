package com.mosl.tabapp.dashboard.leads.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel

interface LeadsReportView : BaseView {
  fun getWidgetSystemsName(): String?
  fun renderData(uiModel: UiModel<Map<String, Any>>)
  fun getAddtionalParameterMap(): Map<String, String>
}