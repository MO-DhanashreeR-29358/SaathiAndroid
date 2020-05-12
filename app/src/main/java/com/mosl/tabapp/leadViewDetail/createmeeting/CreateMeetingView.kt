package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.ClientData

interface CreateMeetingView : BaseView {
  fun render(uiModel: UiModel<ClientData>)
}