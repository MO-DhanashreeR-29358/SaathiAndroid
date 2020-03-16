package com.mosl.tabapp.dashboard.productivity

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.productivity.ProductivityCombo
import io.reactivex.Observable

interface productivityView : BaseView {

  fun onNavigateEvent(): Observable<MutableMap<String, String>>
  fun getWidgetSystemsName(): String?
  fun getAdditionalParameterMap(rowData: String): Map<String, String>
  fun renderData(uiModel: UiModel<ProductivityCombo>)
}