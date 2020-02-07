package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel

interface OTPContract : BaseView {
  fun renderOTPSubmit(uiModel: UiModel<String>)

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UCID = "arg_ucid"
  }

}