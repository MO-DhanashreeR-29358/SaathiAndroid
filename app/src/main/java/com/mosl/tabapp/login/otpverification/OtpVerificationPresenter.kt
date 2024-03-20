package com.mosl.tabapp.login.otpverification

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.rx.addTo
import com.mosl.mobile.user.login.LoginRepository
import com.mosl.mobile.user.model.ResumeOtp
import com.mosl.mobile.user.model.Status
import com.mosl.tabapp.login.otpverification.OtpVerificationView.Companion.TOTAL_RETRY_TIME_SECONDS
import com.mosl.tabapp.login.otpverification.OtpVerificationView.Companion.resendCounter
import javax.inject.Inject
class OtpVerificationPresenter @Inject constructor(
view:OtpVerificationView,
val loginRepository: LoginRepository
):BasePresenter<OtpVerificationView> (view){
    override fun start() {

        onFetchCalled()
            .subscribe { view.startTimer() }
            .addTo(disposable)

        view.fnOnProccedClicked()
            .map {
                view.getOtp()
            }
            .switchMap {
                input->
                SaathiOtpValidator.validate(input.oTP?:"")
                    .flatMap {
                        loginRepository.fnCallValidateOtp(view.getOtp())
                    }.compose(applyUiModel())

            }.subscribe {
                view.fnRenderValidateOtp(it)

            }.addTo(disposable)

        view.onResendClicked()
            .switchMap {
                loginRepository.fnCallOtp( view.fnGetLoginData())
                    .doOnNext { view.startTimer() }
                    .map {
                        ResumeOtp(
                            TOTAL_RETRY_TIME_SECONDS.toInt(),
                            resendCounter = resendCounter,
                            status =Status.SUCCESS,
                            message = it
                        )
                    }.compose(applyUiModel())
            }.subscribe {
                view.renderResend(it)
            }.addTo(disposable)

    }
}