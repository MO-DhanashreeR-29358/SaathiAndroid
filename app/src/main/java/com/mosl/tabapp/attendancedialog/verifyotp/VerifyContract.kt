package com.mosl.tabapp.attendancedialog.verifyotp

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.user.domain.model.GenerateOTP
import com.mosl.user.domain.model.User
import io.reactivex.rxjava3.core.Observable

class VerifyContract {

  interface View : BaseView {
    fun onConfirmClicked(): Observable<Unit>
    fun onDoneClicked(): Observable<Int>

    fun onOtpTextChanged(): Observable<CharSequence>
    fun getOtp(): String

    fun onResendOtpClicked(): Observable<Unit>

    fun renderOtpEntered()
    fun renderTicker(timerState: TimerState)
    fun renderSubmit(uiModel: UiModel<User>)
    fun showPopup()
    fun renderResendOtp(uiModel: UiModel<GenerateOTP>)
  }

  companion object {
    const val OTP_RESENT = 1
  }
}