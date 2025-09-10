package com.mosl.tabapp.bankmeeting.otp

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.lead.SubmitOTPResponse

interface OTPContract : BaseView {
  fun renderOTPSubmit(uiModel: UiModel<SubmitOTPResponse>)

  companion object {
    const val ARG_BANK_ID = "arg_bank_id"
    const val ARG_BANK_BRANCH_CODE = "arg_bank_branch_code"
  }

}