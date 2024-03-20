package com.mosl.tabapp.login.otpverification

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.ResumeOtp
import com.mosl.mobile.user.model.Status
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.ValidateOtpPostModel
import com.mosl.tabapp.login.LoginActivityUiEvent
import io.reactivex.rxjava3.core.Observable

interface OtpVerificationView:BaseView {

    fun startTimer()
    fun onOtpEntered():Observable<LoginActivityUiEvent.OtpEntered>
    fun fnOnProccedClicked():Observable<Unit>

    fun fnRenderValidateOtp(uiModel:UiModel<User>)

    fun onResendClicked(): Observable<Unit>
    fun renderResend(uiModel: UiModel<ResumeOtp>)

    fun getOtp(): ValidateOtpPostModel
    fun fnGetLoginData(): LoginInfo
    companion object{
        const val TOTAL_RETRY_TIME_SECONDS = 60L
        const val STATUS_FAIL = "0"
         const val RETRY_COUNTER_INTERVAL_SECONDS = 1L
       const val resendCounter = 3
    }
}