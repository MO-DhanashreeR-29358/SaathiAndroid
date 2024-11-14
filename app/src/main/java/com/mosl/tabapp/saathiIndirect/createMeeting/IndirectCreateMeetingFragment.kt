package com.mosl.tabapp.saathiIndirect.createMeeting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MoslConstant
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCreateMeetingBinding
import com.mosl.tabapp.databinding.FragIndirectSetAppointmentBinding
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentUiEvent
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class IndirectCreateMeetingFragment:CommonFragment(),IndirectCreateMeetingView {
    internal lateinit var binding:FragIndirectCreateMeetingBinding

    @Inject
    lateinit var presenter: IndirectCreateMeetingPresenter

    private lateinit var activityAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingDurationAdapter:ArrayAdapter<LookUp>
    private lateinit var meetingObjectiveAdapter:ArrayAdapter<LookUp>
    private lateinit var locationAddAdapter:ArrayAdapter<LookUp>
    private lateinit var travellingModeAdapter:ArrayAdapter<LookUp>
    private lateinit var mainFromDate: String
    var selectedDate : String = CRMAppUtil.getDateStr(Date(), MoslConstant.dd_MM_yyyy)
    private val uiSubject = PublishSubject.create<IndirectCreateMeetingUiEvent>()
    override fun getLayout() = R.layout.frag_indirect_create_meeting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCreateMeetingBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        activityAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActivityType.adapter = activityAdapter

        meetingAdapter= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingReason.adapter=meetingAdapter

        meetingDurationAdapter= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingDuration.adapter=meetingDurationAdapter

        meetingObjectiveAdapter=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingObjective.adapter=meetingObjectiveAdapter

        locationAddAdapter=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spLocationAdd.adapter=locationAddAdapter

        travellingModeAdapter=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spTravellingMode.adapter=travellingModeAdapter
    }

    override fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            activityAdapter.setSpinnerData(binding.spActivityType, it)
            uiSubject.onNext(IndirectCreateMeetingUiEvent.OnMeetingDurationCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onActivityTypeSelected(): Observable<Int> {
        return binding.spActivityType.itemSelections()
    }

    override fun getSelectedActivityType(): LookUp {
        return (binding.spActivityType.selectedItem as LookUp)
    }

    override fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            meetingAdapter.setSpinnerData(binding.spMeetingReason, it)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onMeetingDurationCall(): Observable<IndirectCreateMeetingUiEvent.OnMeetingDurationCall> {
        return uiSubject.ofType()
    }

    override fun fnRenderMeetingDuration(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            meetingDurationAdapter.setSpinnerData(binding.spMeetingDuration, it)
            uiSubject.onNext(IndirectCreateMeetingUiEvent.OnMeetingObjectiveCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onMeetingObjectiveCall(): Observable<IndirectCreateMeetingUiEvent.OnMeetingObjectiveCall> {
        return  uiSubject.ofType()
    }

    override fun fnRenderMeetingObjective(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            meetingObjectiveAdapter.setSpinnerData(binding.spMeetingObjective, it)
            uiSubject.onNext(IndirectCreateMeetingUiEvent.OnLocationAddressCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onLocationAddressCall(): Observable<IndirectCreateMeetingUiEvent.OnLocationAddressCall> {
        return  uiSubject.ofType()
    }

    override fun fnRenderLocationAddress(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            locationAddAdapter.setSpinnerData(binding.spLocationAdd, it)
            uiSubject.onNext(IndirectCreateMeetingUiEvent.OnTravellingModeCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onTravellingModeCall(): Observable<IndirectCreateMeetingUiEvent.OnTravellingModeCall> {
        return uiSubject.ofType()
    }

    override fun fnRenderTravellingMode(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            travellingModeAdapter.setSpinnerData(binding.spTravellingMode, it)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }
    override fun onDatePicker(): Observable<Unit> {
        return binding.rlDate.clicks()
    }

    override fun renderDatePicker() {
        showSelectDateDialog()
    }

    private fun showSelectDateDialog() {
        var hour: Int
        var minute: Int

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.date_picker)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        val dpFromDate = dialog.findViewById<DatePicker>(R.id.datePickerFromDate)
        dpFromDate.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        val close =
            dialog.findViewById<ImageView>(R.id.ibtnClose)
        val btnSubmit =
            dialog.findViewById<Button>(R.id.btnSubmit)
        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
        timePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        timePicker.setIs24HourView(true)
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        if (getSelectedActivityType()?.lookUpName.equals("Personal Meeting", ignoreCase = true)
            && userManager.user.isRM()
        ) {
            dpFromDate.minDate = System.currentTimeMillis() - 1000
        }
        else if((getSelectedActivityType()?.lookUpName.equals("Phone", ignoreCase = true) ||getSelectedActivityType()?.lookUpName.equals("Video Conference", ignoreCase = true))
            && userManager.user.currentBusinessProfile.businessUnitId.equals("14")){
            dpFromDate.minDate = System.currentTimeMillis() - 1000
        }
        else {
            dpFromDate.minDate = cal.timeInMillis
        }
        dpFromDate.maxDate = System.currentTimeMillis() - 1000

        try {
            val dateSplit: Array<String> = mainFromDate.split("-").toTypedArray()
            dpFromDate.init(
                dateSplit[0].toInt(),
                dateSplit[1].toInt() - 1,
                dateSplit[2].toInt()
            ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                mainFromDate =
                    year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
            }
        } catch (e: Exception) {
            Timber.d(e)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = hour
            timePicker.minute = minute
        } else {
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }

        timePicker.setOnTimeChangedListener { _: TimePicker?, arg1: Int, arg2: Int ->
            hour = arg1
            minute = arg2
        }

        close.setOnClickListener { dialog.dismiss() }

        btnSubmit.setOnClickListener {
            try {
                val fromDat = CRMAppUtil.getDate(mainFromDate, MoslConstant.yyyy_MM_dd)
                val fromPostingDate = CRMAppUtil.getDateStr(fromDat, MoslConstant.dd_MM_yyyy)
                selectedDate = fromPostingDate
                val finalSelectedDateStr =
                    String.format(
                        "%s %d:%d:00", fromPostingDate, hour,
                        minute
                    ) //dd-MM-yyyy hh:mm:ss
                val finalSelectedDate =
                    CRMAppUtil.getDate(finalSelectedDateStr, MoslConstant.dd_MMM_yyyy_hh_mm)
                if (CRMAppUtil.isBeforeandEqualDate(
                        finalSelectedDate
                    )
                ) { //
                    binding.tvDate.text =
                        CRMAppUtil.getDBDate(finalSelectedDate,
                            MOSLConstants.DATE_FORMAT_TO_DISPLAY
                        )

                    dialog.dismiss()
                } else {
                    MSFDialog.alertDialog(
                        requireContext(), 0,
                        resources.getString(R.string.motilal_oswal), MOSLConstants.INVALID_TIME,
                        "OK", true, null
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()

    }

    fun prefix(input: Int): String? {
        return if (input.toString().length == 1) {
            "0$input"
        } else input.toString() + ""
    }
}