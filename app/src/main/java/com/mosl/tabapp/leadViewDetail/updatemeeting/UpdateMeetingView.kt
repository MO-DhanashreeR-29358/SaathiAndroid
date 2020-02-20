package com.mosl.tabapp.leadViewDetail.updatemeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting

interface UpdateMeetingView : BaseView {

  fun renderMeetingById(uiModel: UiModel<Meeting>)

  companion object {
    const val ARG_UPDATE_MEETING = "arg_update_meeting"
  }
}