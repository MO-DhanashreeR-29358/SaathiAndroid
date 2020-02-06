package com.mosl.mobile.pdf


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mosl.app.coreui.dialog.BaseDialogFragment
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiApi
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentOtpBinding
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPContract
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPEvent
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

    override fun getLayout(): Int {
        return R.layout.fragment_otp
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpBinding.bind(view)

        binding.apply {

            imgCancel.setOnClickListener {
                dialog?.dismiss()
            }

            binding.btnSubmitOtp.setOnClickListener {
                //                if (CRMAppUtil.isNetworkEnabled(requireContext())) {
//
//                    val request = SendOTPRequest(
//                        leadId,
//                        mUCID,
//                        geoArea,
//                        mCurrentLocation.getLongitude().toString() + "",
//                        mCurrentLocation.getLatitude().toString() + "",
//                        "2",
//                        0,
//                        userManager.getUser()!!.employeeCode
//                    )
//                    val disposable: Disposable = saathiApi.getStartMeeting(request)
//                        .compose(applyUiModel<String>())
//                        .subscribe(Consumer<UiModel<String>> { uiModel: UiModel<String?>? ->
//                            this.renderSendOTP(
//                                uiModel
//                            )
//                        })
//                } else {
//                    noInternetConnectionDialog()
//                }
            }

        }


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
            publishSubject: PublishSubject<OTPEvent>
        ): OTPFragment {
            val fragment = OTPFragment()
            return fragment
        }
    }
}
