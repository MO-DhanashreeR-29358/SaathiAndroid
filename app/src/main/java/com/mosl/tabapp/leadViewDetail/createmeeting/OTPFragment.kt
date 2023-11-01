package com.mosl.tabapp.leadViewDetail.createmeeting

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.dialog.BaseDialogFragment
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.SendOtpResponse
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.navigator.Navigator
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.lead.SubmitOTPResponse
import com.mosl.saathi.api.model.lead.createmeeting.PostOTPForMeetingRequest
import com.mosl.saathi.api.model.lead.createmeeting.SendOTPRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.FragmentOtpBinding
import com.mosl.tabapp.ui.MSFDialog
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 */
class OTPFragment : BaseDialogFragment(), OTPContract {

    lateinit var binding: FragmentOtpBinding

    @Inject
    lateinit var userManager: UserManagerImpl

    @Inject
    lateinit var saathiApi: SaathiApi

    private lateinit var navigator: Navigator
    private lateinit var callBack: OTPDialogListener;

    var leadId = "0"
    var mUCID = "0"
    var mobileNumber = "0"
    var randomInt = 0
    private val otpEvent = PublishSubject.create<Boolean>()
    override fun getLayout(): Int {
        return R.layout.fragment_otp
    }

    override fun extractArguments(bundle: Bundle) {
        try {
            leadId = bundle.getString(OTPContract.ARG_LEAD_ID, "0")
            mUCID = bundle.getString(OTPContract.ARG_UCID, "0")
            mobileNumber = bundle.getString(OTPContract.ARG_MOBILE_NUMBER, "0")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpBinding.bind(view)
        dialog?.apply {
            setCanceledOnTouchOutside(false)
        }
        isCancelable = false
        binding.apply {
            imgCancel.setOnClickListener {
                callBack.cancelClick()
                dismiss()
            }
            btnSubmitOtp.setOnClickListener {
                if (etOtp.text?.length == 4 || etOtp.text?.length == 6) {
                    submitOtp()
                } else {
                    alert("Please enter valid OTP")
                }
            }
            btnResendOtp.setOnClickListener {
                resendOTPRequest()
            }
        }

    }

    private fun resendOTPRequest() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            val resendOtpFlag = 1
            val request = SendOTPRequest(
                leadId, mobileNumber, mUCID, "",
                "",
                "", "2", resendOtpFlag, userManager.getUser()!!.employeeCode
            )
            val disposable = saathiApi.getStartMeeting(request)
                .compose(applyUiModel())
                .subscribe { this.renderResendOTP(it) }
        } else {
            noInternetConnectionDialog()
        }

    }

    private fun renderResendOTP(uiModel: UiModel<SaathiCommonResponse<String>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("OTPFragment renderResendOTP showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            Toast.makeText(requireContext(), "Resend OTP successfully", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun submitOtp() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            randomInt = Random.nextInt()
            val request = PostOTPForMeetingRequest(
                employeeCode = userManager.getUser()!!.employeeCode,
                leadId = leadId,
                uCID = mUCID,
                oTP = binding.etOtp.text.toString(),
                payLoad = randomInt
            )
            val disposable: Disposable = saathiApi.postOtpForMeeting(request)
                .compose(applyUiModel())
                .subscribe(::renderOTPSubmit)
        } else {
            noInternetConnectionDialog()
        }
    }

    fun setListener(callBack: OTPDialogListener) {
        this.callBack = callBack
    }

    override fun renderOTPSubmit(uiModel: UiModel<SubmitOTPResponse>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            callBack.onSuccessResult(false)
            Timber.d("OTPFragment renderOTPSubmit showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            if (it.payLoad.toInt() == randomInt) {
                Toast.makeText(requireContext(), "Verify OTP successfully", Toast.LENGTH_SHORT)
                    .show()
                callBack.onSuccessResult(true)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "OTP is invalid", Toast.LENGTH_SHORT)
                    .show()
                callBack.onSuccessResult(false)
                dialog?.dismiss()
            }
        }
    }

    fun showOnFailurePopup(t: Throwable) {
        val onFailure = getErrorOnFailure(t)
        Timber.d("OTPFragment showOnFailurePopup showOnFailurePopup ${t.message}")

        showMessagePopup(onFailure)
    }

    fun showMessagePopup(error: ErrorModel) {
        alert(message = error.getResolvedMessage(requireContext()))
    }

    fun alert(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_dialog_title)
            .setMessage(message)
            .setPositiveButton(R.string.button_label_ok, null)
            .show()
    }

    fun noInternetConnectionDialog() {
        MSFDialog.alertDialog(
            requireContext(),
            0,
            resources.getString(R.string.motilal_oswal),
            MOSLConstants.INTERNET_CONNECTION,
            "OK",
            true,
            null
        )
    }

    fun toggleProgress(show: Boolean) {
        if (show) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    fun showProgress() {
        navigator.showProgress()
    }

    fun hideProgress() {
        navigator.hideProgress()
    }

    override fun onAttach(context: Context) {
        this.navigator = context as Navigator
        super.onAttach(context)
    }

    fun getErrorOnFailure(throwable: Throwable): ErrorModel {
        return throwable.getErrorOnFailure()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    companion object {
        @JvmStatic
        fun newInstance(
            leadId: String,
            ucid: String,
            mobileNumber: String
        ): OTPFragment {

            val args = Bundle().apply {
                putString(OTPContract.ARG_LEAD_ID, leadId)
                putString(OTPContract.ARG_UCID, ucid)
                putString(OTPContract.ARG_MOBILE_NUMBER, mobileNumber)
            }

            return OTPFragment().apply {
                arguments = args
            }
        }
    }

    interface OTPDialogListener {
        fun onSuccessResult(status: Boolean)
        fun cancelClick()
    }

}
