package com.mosl.tabapp.attendancedialog.verifyotp

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.CharSequenceToString
import com.mosl.tabapp.attendancedialog.verifyotp.TimerState.Ended
import com.mosl.tabapp.attendancedialog.verifyotp.TimerState.InProgress
import com.mosl.tabapp.attendancedialog.verifyotp.VerifyContract.View
import com.mosl.user.data.login.VerifyAction
import com.mosl.user.domain.LoginRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class VerifyPresenter @Inject constructor(
    protected val repository: LoginRepository,
    protected val verifyAction: VerifyAction,
    view: View
) : BasePresenter<View>(view) {
  val MaxCount = 20L
  val OtpLength = 6

  override fun start() {
//    view.setOtp(verifyAction.otp.toString())
    val otpReceivedObs = view.onOtpReceived()
        .share()

    val validOtpEntered = view.onOtpTextChanged()
        .map(CharSequenceToString)
        .filter { it.length == OtpLength }
        .share()

    disposable.add(validOtpEntered.subscribe { view.renderOtpEntered() })

    disposable.add(otpReceivedObs.subscribe { view.setOtp(it.toString()) })

    disposable.add(view.onResendOtpClicked()
        .switchMap {
          repository.userAuthentication(verifyAction.mobileNumber)
              .compose(applyUiModel())
        }.subscribe(view::renderResendOtp))

    disposable.add(
        Observable.merge(onFetchCalled(), onRetrySection(VerifyContract.OTP_RESENT))
            .switchMap {
              Observable.intervalRange(1L, MaxCount, 0, 1000, MILLISECONDS)
                  .map {
                    val count = MaxCount - it
                    if (count == 0L) {
                      Ended
                    } else {
                      InProgress(count)
                    }
                  }
                  .takeUntil(validOtpEntered)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::renderTicker))

    disposable.add(
        Observable.merge(view.onConfirmClicked(), view.onDoneClicked())
            .map { view.getOtp() }
            .filter { it.length == OtpLength }
            .map { it.toInt() }
            .mergeWith(otpReceivedObs)
            .map { VerifyAction(verifyAction.requestId, verifyAction.mobileNumber, view.getOtp()) }
            .switchMap {
              repository.verifyOtp(it)
                  .compose(applyUiModel())
            }
            .subscribe(view::renderSubmit)

    )
  }

}

sealed class TimerState {
  data class InProgress(val count: Long) : TimerState()
  object Ended : TimerState()
}