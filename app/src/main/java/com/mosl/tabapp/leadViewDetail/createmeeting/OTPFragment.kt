package com.mosl.mobile.pdf

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
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.navigator.Navigator
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.lead.createmeeting.PostOTPForMeetingRequest
import com.mosl.saathi.api.model.lead.createmeeting.SendOTPRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.FragmentOtpBinding
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPContract
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPEvent
import com.mosl.tabapp.ui.MSFDialog
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

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

  var leadId = "0"
  var mUCID = "0"

  override fun getLayout(): Int {
    return R.layout.fragment_otp
  }

  override fun extractArguments(bundle: Bundle) {
    try {
      leadId = bundle.getString(OTPContract.ARG_LEAD_ID, "0")
      mUCID = bundle.getString(OTPContract.ARG_UCID, "0")
    } catch (e: Exception) {
      e.printStackTrace();
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
        dismiss()
      }
      btnSubmitOtp.setOnClickListener {
        submitOtp()
      }
      tvResendOtp.setOnClickListener {
        resendOTPRequest()
      }
    }

  }

  private fun resendOTPRequest() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val resendOtpFlag = 1
      val request = SendOTPRequest(
          leadId, mUCID, "",
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

  private fun renderResendOTP(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      Toast.makeText(requireContext(), "Resend OTP Successfully", Toast.LENGTH_SHORT)
          .show()
    }
  }

  private fun submitOtp() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {

      val request = PostOTPForMeetingRequest(
          employeeCode = userManager.getUser()!!.employeeCode.toInt(),
          leadId = leadId,
          uCID = mUCID,
          oTP = binding.etOtp.text.toString()
      )
      val disposable: Disposable = saathiApi.postOtpForMeeting(request)
          .compose(applyUiModel<String>())
          .subscribe { uiModel: UiModel<String> ->
            renderOTPSubmit(uiModel)
          }
    } else {
      noInternetConnectionDialog()
    }
  }

  override fun renderOTPSubmit(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) showOnFailurePopup(error)

    if (uiModel.data != null) {
      dialog?.dismiss()
    }
  }

  fun showOnFailurePopup(t: Throwable) {
    val onFailure = getErrorOnFailure(t)
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
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION, "OK", true,
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

    const val PDF_LINK = "pdf_link"
    @JvmStatic
    fun newInstance(
      publishSubject: PublishSubject<OTPEvent>,
      leadId: String,
      ucid: String
    ): OTPFragment {

      val args = Bundle().apply {
        putString(OTPContract.ARG_LEAD_ID, leadId)
        putString(OTPContract.ARG_UCID, ucid)
      }

      return OTPFragment().apply {
        arguments = args
      }
    }
  }
}
