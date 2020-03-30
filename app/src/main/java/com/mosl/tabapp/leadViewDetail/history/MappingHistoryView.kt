package com.mosl.tabapp.leadViewDetail.history

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryCombo
import io.reactivex.Observable

interface MappingHistoryView : BaseView {
  fun onEventTypeChange(): Observable<Int>
  fun getEventType(): String
  fun getLeadId(): String
  fun getUcId(): String
  fun renderData(uiModel: UiModel<MappingHistoryCombo>)
}