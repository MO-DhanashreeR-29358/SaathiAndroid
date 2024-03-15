package com.mosl.tabapp.login.otpverification

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.ErrorState
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.utils.alert
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.ekycexpress.user.model.emptyString
import com.mosl.mobile.user.model.LoginInfo
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
import com.mosl.validator.exception.ValidationException
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.mosl.commons.rx.ofType
import com.mosl.tabapp.login.LoginActivity
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import io.reactivex.rxjava3.kotlin.addTo
import java.lang.Exception

class OtpVerificationFragment:BottomSheetDialogFragment(),OtpVerificationView, OTPListener {

    lateinit var binding:LayoutOtpVerifyFragmentBinding
    @Inject
    lateinit var presenter: OtpVerificationPresenter
    protected val dataActionSubject: PublishSubject<DataAction> = PublishSubject.create()
    private val subject = LoginActivityUiEvent.getLoginUIEvent()
    val disposable = CompositeDisposable()
    var objLoginInfo: LoginInfo?=null

    private val handler = Handler()
    var intCount:String ="1"
    var strOtpMessage:String=""

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

    override fun onDestroy() {
        super.onDestroy()
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
            if(strOtpMessage.isNotEmpty())
            {
                intCount=strOtpMessage.substringAfter(":-")
                binding.tvEnterOtpMsg.setText(strOtpMessage.substringBefore(" |"))
                binding.tvEnterOtpMsg.isVisible=true
                binding.tvCounterOtp.setText("$intCount / 3 OTP Sent")

            }
        else
        {
            binding.tvEnterOtpMsg.setText("")
                binding.tvEnterOtpMsg.isVisible=false
            }
    }

    override fun fnGetLoginData(): LoginInfo {
        return objLoginInfo?: LoginInfo("","","")
    }
     fun fnSetData(_objLoginInfo:LoginInfo,strotpmessage:String)
    {
        objLoginInfo=LoginInfo(_objLoginInfo.empCode,_objLoginInfo.password,_objLoginInfo.appVersion)
        strOtpMessage=strotpmessage
    }
    override fun startTimer() {
        try {
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
                        text = "00:${String.format("%02d", remainingSec)}"
                    }
                    binding.tvCounter.visibility =
                        if (remainingSec > 0) View.VISIBLE else View.INVISIBLE
                }.addTo(disposable)
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
            Log.d("error",ex.message.toString())
        }

    }

    private fun toggleResend(enable: Boolean) {
        if (enable){
            binding.ekycJetOtpError.text = emptyString
            binding.tvResendOtp.isEnabled = enable
            binding.tvResendOtp.setTextColor(resources.getColor(R.color.light_red))
          /*  parentFragment?.requireContext()?.getColor(R.color.light_red)?.let {
                binding.tvResendOtp.setTextColor(
                    it
                )
            }*/
        }else{
            binding.tvResendOtp.isEnabled = enable
            binding.tvResendOtp.setTextColor(resources.getColor(R.color.button_grey))
            /*parentFragment?.requireContext()?.getColor(R.color.button_grey)?.let {
                binding.tvResendOtp.setTextColor(
                    it
                )
            }*/
        }
    }

    override fun onInteractionListener() {
        Timber.i("Otp Sheet 1 ${binding.ekycJetOtpView.otp.toString()}")
        if (binding.ekycJetOtpError.text.isNullOrEmpty().not()){
            binding.ekycJetOtpError.text = emptyString
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
        (activity as LoginActivity ).toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure {
            if (it is ValidationException) {
                val error = it.error.message
                showValidationError(error.orEmpty())
            } else {
                val message = it.getErrorOnFailure().message
                if (message.isNullOrEmpty().not()) {
                    showValidationError(message.orEmpty())
                }
            }
        }

        uiModel.onSuccess {
            if (it.status != Status.SUCCESS) {
                (activity as LoginActivity ).fnShowAlert(it.message)
                return
            }
            binding.ekycJetOtpView.setOTP(emptyString)
            binding.ekycJetOtpError.text = emptyString
            if(it.message.isNotEmpty())
            {
                intCount=it.message.substringAfter(":-")
                binding.tvEnterOtpMsg.setText(it.message.substringBefore(" |"))
                binding.tvEnterOtpMsg.isVisible=true
                binding.tvCounterOtp.setText("$intCount / 3 OTP Sent")

            }
            else
            {
                binding.tvEnterOtpMsg.setText("")
                binding.tvEnterOtpMsg.isVisible=false
            }
            //startOtpListener()
        }
    }

    override fun getOtp(): ValidateOtpPostModel {
        return  ValidateOtpPostModel(
            ipaddress = "",  isPersistance = false, employeeCode = objLoginInfo?.empCode , macaddress = "",
            appId = 2,clientinfo = "", oTP=binding.ekycJetOtpView.otp.toString(), password=objLoginInfo?.password
        )
    }
    override fun fnOnProccedClicked(): Observable<Unit> {
        return binding.ekycJetbtnProceed.clicks().doOnNext { binding.ekycJetbtnProceed.isEnabled = false
            binding.ekycJetOtpError.text = emptyString }
    }

    override fun fnRenderValidateOtp(uiModel: UiModel<User>) {
        uiModel.onSuccess {
            binding.ekycJetbtnProceed.isEnabled = true
            binding.tvEnterOtpMsg.text="OTP Validated"
            handler.postDelayed({
                dismiss()
                subject.onNext(LoginActivityUiEvent.OnOtpSucess(it))
            }, 1000)

        }
        uiModel.onFailure {
            binding.ekycJetbtnProceed.isEnabled = true
            binding.tvEnterOtpMsg.setText("")
            binding.tvEnterOtpMsg.isVisible=false
            if (it is ValidationException) {
                val error = it.error.message
                showValidationError(error.orEmpty())
            } else {
                val message = it.getErrorOnFailure().message
                if (message.isNullOrEmpty().not()) {
                    showValidationError(message.orEmpty())
                }
            }
        }
    }

    private fun showValidationError(error: String){
        binding.ekycJetOtpError.text = error
    }

}