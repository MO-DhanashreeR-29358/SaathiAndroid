package com.mosl.tabapp.login.otpverification

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.user.login.LoginRepository
import io.reactivex.rxjava3.kotlin.addTo
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
                loginRepository.fnCallValidateOtp(it).compose(applyUiModel())
            }.subscribe {
                view.fnRenderValidateOtp(it)
            }

    }
}