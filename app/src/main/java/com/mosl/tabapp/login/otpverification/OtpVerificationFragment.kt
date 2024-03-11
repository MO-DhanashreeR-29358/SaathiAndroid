package com.mosl.tabapp.login.otpverification

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.ErrorState
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.ekycexpress.user.model.emptyString
import com.mosl.mobile.user.model.ResumeOtp
import com.mosl.mobile.user.model.Status
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.ValidateOtpPostModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.LayoutOtpVerifyFragmentBinding
import com.mosl.tabapp.login.LoginActivityUiEvent
import com.mosl.tabapp.login.otpverification.OtpVerificationView.Companion.RETRY_COUNTER_INTERVAL_SECONDS
import com.mosl.tabapp.login.otpverification.OtpVerificationView.Companion.TOTAL_RETRY_TIME_SECONDS
import com.mosl.tabapp.login.otpverification.helper.otptextview.OTPListener
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtpVerificationFragment:BottomSheetDialogFragment(),OtpVerificationView, OTPListener {

    lateinit var binding:LayoutOtpVerifyFragmentBinding
    @Inject
    lateinit var presenter: OtpVerificationPresenter
    protected val dataActionSubject: PublishSubject<DataAction> = PublishSubject.create()
    private val subject = PublishSubject.create<LoginActivityUiEvent>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.layout_otp_verify_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutOtpVerifyFragmentBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        fnSetView()
        binding.ekycClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun getTheme(): Int {
        return R.style.EkycAppBottomSheetDialogTheme
    }

    override fun isDataEmpty(): Boolean {
        return false
    }

    override fun onDataAction(): Observable<DataAction> {
        return dataActionSubject
    }
    private fun fnSetView()
    {

    }

    override fun startTimer() {
        val counter = TOTAL_RETRY_TIME_SECONDS - RETRY_COUNTER_INTERVAL_SECONDS

        Observable.interval(RETRY_COUNTER_INTERVAL_SECONDS, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { toggleResend(false) }
            .take(TOTAL_RETRY_TIME_SECONDS)
            .doOnComplete {
                toggleResend(true)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { sec ->
                val remainingSec = counter - sec
                binding.tvCounter.apply {
                    text = "00:${String.format("%02d",remainingSec)}"
                }
                binding.tvCounter.visibility = if (remainingSec > 0) View.VISIBLE else View.INVISIBLE
            }.addTo(disposable)

    }

    private fun toggleResend(enable: Boolean) {
        if (enable){
            binding.ekycJetOtpError.text = emptyString
            binding.tvResendOtp.isEnabled = enable
            parentFragment?.requireContext()?.getColor(com.mosl.ekycexpress.login.R.color.ekyc_bright_orange)?.let {
                binding.tvResendOtp.setTextColor(
                    it
                )
            }
        }else{
            binding.tvResendOtp.isEnabled = enable
            parentFragment?.requireContext()?.getColor(com.mosl.ekycexpress.login.R.color.ekyc_text_color_grey)?.let {
                binding.tvResendOtp.setTextColor(
                    it
                )
            }
        }
    }

    override fun onOTPComplete(otp: String) {

        if (otp.length == 4){
            binding.ekycJetbtnProceed.isEnabled = false
            subject.onNext(LoginActivityUiEvent.OtpEntered)
        }
    }
    override fun onOtpEntered(): Observable<LoginActivityUiEvent.OtpEntered> {
        return subject.ofType()
    }

    override fun onResendClicked(): Observable<Unit> {
        return binding.tvResendOtp.clicks()
    }

    override fun renderResend(uiModel: UiModel<ResumeOtp>) {
        (parentFragment as BaseFragment).toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure {
            //showValidationError(it.message.orEmpty())
        }

        uiModel.onSuccess {
            if (it.status != Status.SUCCESS) {
                (parentFragment as BaseFragment).showMessagePopup(ErrorModel(it.message, ErrorState.SERVICE_ERROR))
                return
            }
            binding.ekycJetOtpView.setOTP(emptyString)
            binding.ekycJetOtpError.text = emptyString
            //startOtpListener()
        }
    }

    override fun getOtp(): ValidateOtpPostModel {
        return  ValidateOtpPostModel(
            ipaddress = "192.168.62.99",  isPersistance = false, employeeCode = "10640", macaddress = "123456789",
            appId = 2,clientinfo = "IE 1234567", oTP="12345", password="mosl_123"
        )
    }
    override fun fnOnProccedClicked(): Observable<Unit> {
        return binding.ekycJetbtnProceed.clicks().doOnNext { binding.ekycJetbtnProceed.isEnabled = false
            binding.ekycJetOtpError.text = emptyString }
    }

    override fun fnRenderValidateOtp(uiModel: UiModel<User>) {
        uiModel.onSuccess {

        }
        uiModel.onFailure {error->
           Log.d("error","otp error")
        }
    }
}