package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.LookUp
import io.reactivex.Observable

interface CreateMeetingView : BaseView {
  fun render(uiModel: UiModel<ClientData>)
  fun renderAddress(uiModel: UiModel<String>)
  fun renderMeetingReason(uiModel: UiModel<List<LookUp>>)

  fun onMeetingLocationSelected(): Observable<Int>
  fun onActivityTypeSelected(): Observable<Int>
  fun getSelectedMeetingLocation(): LookUp?
  fun getSelectedActivityType(): LookUp?
}