package com.mosl.tabapp.attendancedialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.mobile.user.login.UserManager
import com.mosl.shared.util.AppDateFormats
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentAttendanceDialogBinding
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import com.mosl.shared.util.DateParser
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AttendanceDialogFragment : DialogFragment(), AttendanceDialogView {

    lateinit var binding: FragmentAttendanceDialogBinding

    @Inject
    lateinit var presenter: AttendanceDialogPresenter

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var screenNavigator: ScreenNavigatorImpl
    val dataActionSubject = PublishSubject.create<DataAction>()
    lateinit var skipListener: SkipListener
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attendance_dialog, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAttendanceDialogBinding.bind(view)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            for (data in getAnimationViewData()) {
                data.first.apply {
                    setAnimation(data.second)
                    repeatCount = INFINITE
                    playAnimation()
                }
                if (data.first == binding.linearMarkAttendance.emojiSad) {
                    val padding = resources.getDimensionPixelSize(R.dimen.padding_small)
                    data.first.setPadding(padding, padding, padding, padding)
                }
            }
        } else {
            for (data in getAnimationViewData()) {
                data.first.setImageResource(data.third)
            }
        }
    }

    fun onSkipEvent(skipListener: SkipListener) {
        this.skipListener = skipListener
    }

    fun getAnimationViewData(): ArrayList<Triple<LottieAnimationView, String, Int>> {
        val animationView = ArrayList<Triple<LottieAnimationView, String, Int>>(3)
        animationView.add(
            Triple(
                binding.linearMarkAttendance.emojiHappy, "happy_emoji.json",
                R.drawable.ic_happy
            )
        )
        animationView.add(
            Triple(
                binding.linearMarkAttendance.emojiExcited, "excited_emoji.json",
                R.drawable.ic_excited
            )
        )
        animationView.add(
            Triple(
                binding.linearMarkAttendance.emojiSad, "sad_emoji.json",
                R.drawable.ic_sad
            )
        )

        return animationView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun showProgress(show: Boolean) {
        val isProgress = if (show) View.VISIBLE
        else View.GONE
        binding.progressBar.visibility = isProgress
        binding.tvErrorMsg.visibility = View.GONE
    }

    override fun renderLogAttendancePopup(uiModel: UiModel<String>, action: String?) {
        showProgress(uiModel.inProgress)
        uiModel.error?.let {
            Timber.e(uiModel.error)
            renderEmpAction(action)
        }
        uiModel.data?.let {
            renderEmpAction(action)
        }
    }

    override fun renderEmpAction(action: String?) {
        val date = Date()
        val time = AppDateFormats.df_Time12.format(date)
        binding.tvLoginDate.text = DateParser.formatToMM(date)
        binding.tvLoginTime2.text = time
        userManager.getUser()
            ?.let {
                binding.txtName.text = String.format("Hi, %s", it.firstName + " " + it.lastName)
            }
        if (action != null) {
            when (action) {
                AttendanceDialogView.SIGN_OUT -> {
                    binding.tvMessage.text = setSpannable(getString(R.string.text_label_sign_out_msg))
                }
                AttendanceDialogView.SIGN_IN -> {
                    val message = getString(R.string.text_label_your_sigin_time) + " " + time
                    binding.tvMessage.text = message
                    binding.btnSubmit.visibility = View.GONE
                }
                AttendanceDialogView.COLLECTION_OFFICER -> {
                    binding.tvMessage.visibility = View.GONE
                    binding.tvCollectionOfficerMessage.visibility = View.VISIBLE
                    binding.btnSubmit.setText(R.string.ekyc_btn_ok)
                    binding.linearMarkAttendance.moodView.visibility = View.GONE
                }
            }
        }
        binding.tvSkip.setOnClickListener {
            skipListener.skipClick(true)
           // (requireActivity() as CommonActivity).getProfileInfo()
            dismiss()
        }
        binding.tvSkip.text = setUnderLineSpannable(getString(R.string.ekyc_btn_label_skip))
    }

    private fun setUnderLineSpannable(
        text: String
    ): Spannable {
        val spannable = SpannableString(text)
        spannable.setSpan(
            UnderlineSpan(),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun setSpannable(time: String): Spannable {
        val size = time.length

        val spannable = SpannableString(time)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.colorPrimaryAttendance)),
            size - 8, size, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    override fun renderSubmitResponse(uiModel: UiModel<String>) {
        showProgress(uiModel.inProgress)
        uiModel.onFailure { error ->
            binding.tvErrorMsg.apply {
                text = error.message
                visibility = View.VISIBLE
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT)
                    .show()
                dismiss()
                // (requireActivity() as CommonActivity).getProfileInfo()
                screenNavigator.openHome()
            }
        }

        uiModel.data?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    override fun onSubmitClick(): Observable<Unit> {
        return binding.btnSubmit.clicks()
    }

    override fun happySelected(): Observable<Unit> {
        return binding.linearMarkAttendance.emojiHappy.clicks()
    }

    override fun excitedSelected(): Observable<Unit> {
        return binding.linearMarkAttendance.emojiExcited.clicks()
    }

    override fun sadSelected(): Observable<Unit> {
        return binding.linearMarkAttendance.emojiSad.clicks()
    }


    override fun isDataEmpty(): Boolean {
        return false
    }

    override fun onDataAction(): Observable<DataAction> {
        return dataActionSubject
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    interface SkipListener {
        fun skipClick(status: Boolean)
    }

    companion object {
        const val ARGS_EMP_ACTION = "action"
        const val ARGS_USER_LAT = "user_lat"
        const val ARGS_USER_LONG = "user_long"
        const val ARGS_USER_AREA = "user_area"
        const val ARGS_USER_LOGIN_BRANCH = "user_login_branch"
    }
}