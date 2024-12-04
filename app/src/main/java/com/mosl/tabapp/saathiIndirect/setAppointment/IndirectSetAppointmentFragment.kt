package com.mosl.tabapp.saathiIndirect.setAppointment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.user.login.user
import com.mosl.shared.util.AppDateFormats
import com.mosl.shared.util.getFormatedDate
import com.mosl.tabapp.R
import com.mosl.tabapp.bankmeeting.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MoslConstant
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectSetAppointmentBinding
import com.mosl.tabapp.leadViewDetail.createmeeting.AttendeeAdapter
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.model.MeetingAttendeeItem
import com.mosl.tabapp.model.SetAppointmentPostReq
import com.mosl.tabapp.model.SplMeetingAttendeeItem
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingUiEvent
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class IndirectSetAppointmentFragment:CommonFragment(),IndirectSetAppointmentView  {
    internal lateinit var binding:FragIndirectSetAppointmentBinding
    @Inject
    lateinit var presenter: IndirectSetAppointmentPresenter
    private lateinit var activityAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingAdapter:ArrayAdapter<LookUp>
    private val uiSubject = PublishSubject.create<IndirectSetAppointmentUiEvent>()
    private lateinit var mainFromDate: String
    var selectedDate : String = CRMAppUtil.getDateStr(Date(), MoslConstant.dd_MM_yyyy)

    private lateinit var attendeeDialog: Dialog
    private lateinit var etSearchAttendeeText: DelayAutoCompleteTextView
    private lateinit var attendeeAdapter: AttendeeAdapter
    private var pbLoadingIndicator: ProgressBar? = null
    private val addedAttendeeList: ArrayList<AddedAttendee> = ArrayList()
    private var addedAttendeeCount = 0


    private lateinit var splAttendeeDialog: Dialog
    private lateinit var etSearchSplAttendeeText: DelayAutoCompleteTextView
    private lateinit var splAttendeeAdapter: AttendeeAdapter
    private var pbLoadingIndicator1: ProgressBar? = null
    private val addedSplAttendeeList: ArrayList<AddedAttendee> = ArrayList()
    private var addedSplAttendeeCount = 0
    var leadID:String=""
    var UcId:String=""


    private var createMode = true
    private var meetingId: String? = ""
    private var meetingApptModel: MeetingApptModel? = null

    override fun getLayout() = R.layout.frag_indirect_set_appointment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectSetAppointmentBinding.bind(view)
        hideAppTitle()
        presenter.start()
        setupViews()
        setUpFromArguments()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

        activityAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActivityType.adapter = activityAdapter

        meetingAdapter= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spmeetingReason.adapter=meetingAdapter
        mainFromDate = CRMAppUtil.getDate(Date(), MoslConstant.yyyy_MM_dd)

        binding.tvApppointmentDateTimeDisplay.text = CRMAppUtil.getDBDate(Date(), MOSLConstants.DATE_FORMAT_TO_DISPLAY)
    }

    private fun setUpFromArguments() {
        try {
            leadID =this.arguments?.getString(MOSLConstants.LEAD_ID, "0") ?: "0"
            UcId =this.arguments?.getString(MOSLConstants.UCID, "0") ?: "0"
            meetingId = this.arguments?.getString(MOSLConstants.MEETING_ID, "0") ?: "0"
            if(meetingId != null && !meetingId.equals("0", ignoreCase = true)) {
                //Editt mode
                createMode = false
                binding.tvNewAppointment.text = getString(R.string.txt_label_reschedule_appointment)
                uiSubject.onNext(IndirectSetAppointmentUiEvent.GetMeeting(meetingId!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun getLeadId(): String {
        return  leadID;
    }

    override fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.layoutTopSection.tvLeadId.text = it.id
            it.leadProducts
                .forEach {
                    if (it.isDefault) {
                        binding.layoutTopSection.tvClientCode.text = it.clientCode
                        binding.layoutTopSection.tvCurrentStage.text = it.stageName
                    }
                }
//            if (clientDetail.currentStageSince.isNotEmpty()) {
//                binding.layoutTopSection.tvCurrentStageSince.text =
//                    String.format("%s Days", clientDetail.currentStageSince)
//            }
            binding.layoutTopSection.tvName.text = it.leadName
            binding.layoutTopSection.tvBranchCode.text = it.branchCode

//            val imageUrl = Base64.decode(it., Base64.DEFAULT)
//            if (imageUrl != null) {
//                CRMAppUtil.setImage(imageUrl, binding.layoutTopSection.ivLead, requireContext())
//            }
        }
        uiSubject.onNext(IndirectSetAppointmentUiEvent.onActivityCall)
    }

    override fun ononActivityCall(): Observable<IndirectSetAppointmentUiEvent.onActivityCall> {
        return  uiSubject.ofType()
    }
    override fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            activityAdapter.setSpinnerData(binding.spActivityType, it)
            val index = it.indexOfFirst { it.lookUpID.toString() == meetingApptModel?.activityTypeID }
            binding.spActivityType.setSelection(index)
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

    override fun getSelectedMeetingReason(): LookUp {
        return (binding.spmeetingReason.selectedItem as LookUp)
    }

    override fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            meetingAdapter.setSpinnerData(binding.spmeetingReason, it)
            val index = it.indexOfFirst { it.lookUpID == meetingApptModel?.meetingReasonID }
            binding.spmeetingReason.setSelection(index)
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
                    binding.tvApppointmentDateTimeDisplay.text =
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

    override fun onAttendeeClicked(): Observable<Unit> {
        return binding.ibAttendee.clicks()
    }

    override fun onAttendeeAddedClicked(): Observable<Unit> {
        return binding.tvAttendeeCount.clicks()
    }

    override fun showAttendeDialog() {
        attendeeDialog = Dialog(requireContext())
        attendeeDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_attendee_list)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
        }
        etSearchAttendeeText = attendeeDialog.findViewById(R.id.etSearchAttendee)
        etSearchAttendeeText.imeOptions = EditorInfo.IME_ACTION_DONE
        etSearchAttendeeText.threshold = 3
        attendeeAdapter = AttendeeAdapter(requireContext())
        etSearchAttendeeText.setAdapter(attendeeAdapter)
        pbLoadingIndicator = attendeeDialog.findViewById(R.id.pbLoadingIndicator)
        etSearchAttendeeText.setLoadingIndicator(pbLoadingIndicator)
        etSearchAttendeeText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                uiSubject.onNext(IndirectSetAppointmentUiEvent.SearchTextChange(s?.toString() ?: ""))
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
        })
        etSearchAttendeeText.setOnItemClickListener { _, _, position, _ ->
            uiSubject.onNext(IndirectSetAppointmentUiEvent.AttendeeItemClick(position))
        }
        attendeeDialog.show()
    }

    override fun getAttendeeSearchText(): Observable<IndirectSetAppointmentUiEvent.SearchTextChange> {
        return  uiSubject.ofType()
    }

    override fun onAttendeeItemClickEventFired(): Observable<IndirectSetAppointmentUiEvent.AttendeeItemClick> {
        return  uiSubject.ofType()
    }
    override fun toggleSearchState(
        queryEmpty: Boolean,
        isAttendee: Boolean
    ) {
        if (queryEmpty) {
            if (isAttendee) {
                attendeeAdapter.setResultList(emptyList())
            } else {
                splAttendeeAdapter.setResultList(emptyList())
            }
        }
    }

    override fun renderSearchResult(
        uiModel: UiModel<List<Attendee>>,
        isAttendee: Boolean
    ) {
        toggleSearchProgress(uiModel.inProgress, isAttendee)

        uiModel.onFailure {
            Log.d("Searchfail attend",Gson().toJson(it))
            if (isAttendee) {
                attendeeAdapter.setErrorData(it)
            } else {
                splAttendeeAdapter.setErrorData(it)
            }
        }

        uiModel.onSuccess {
            Log.d("Search attend",Gson().toJson(it))
            if (isAttendee) {

                attendeeAdapter.setResultList(it)
                if (it.isNotEmpty()) {

                    etSearchAttendeeText.showDropDown()
                }
            } else {
                splAttendeeAdapter.setResultList(it)
                if (it.isNotEmpty()) {
                    etSearchSplAttendeeText.showDropDown()
                }
            }
        }
    }

    override fun renderOnAttendeeItemClick(position: Int) {
        val attendee = attendeeAdapter.getItem(position)
        if (!CreateMeetingConstants.hasAttendeeAdded(addedAttendeeList, attendee.empCode)) {
            addedAttendeeList.add(AddedAttendee(attendee.empName, attendee.empCode, "R"))
            addedAttendeeCount++
            setCount(addedAttendeeCount, binding.tvAttendeeCount)
            attendeeDialog.dismiss()
        } else {
            attendeeDialog.dismiss()
            Toast.makeText(requireContext(), "Attendee Already Added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun toggleSearchProgress(
        show: Boolean,
        isAttendee: Boolean
    ) {
        if (show) {
            if (isAttendee) {
                pbLoadingIndicator?.visibility = View.VISIBLE
            } else {
                pbLoadingIndicator1?.visibility = View.VISIBLE
            }
        } else {
            if (isAttendee) {
                pbLoadingIndicator?.visibility = View.GONE
            } else {
                pbLoadingIndicator1?.visibility = View.GONE
            }
        }
    }

    private fun setCount(
        count: Int,
        textView: TextView
    ) {
        if (count <= 0) {
            textView.text = ""
        } else {
            textView.text = String.format("(%s)", count.toString())
        }
    }

    override fun onSplAttendeeClicked(): Observable<Unit> {
        return binding.ibspAttendee.clicks()
    }

    override fun onSplAttendeeAddedClicked(): Observable<Unit> {
        return binding.tvspAttendeeCount.clicks()
    }

    override fun showSplAttendeDialog() {
        splAttendeeDialog = Dialog(requireContext())
        splAttendeeDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_attendee_list)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
        }
        etSearchSplAttendeeText = splAttendeeDialog.findViewById(R.id.etSearchAttendee)
        etSearchSplAttendeeText.imeOptions = EditorInfo.IME_ACTION_DONE
        etSearchSplAttendeeText.threshold = 3
        splAttendeeAdapter = AttendeeAdapter(requireContext())
        etSearchSplAttendeeText.setAdapter(splAttendeeAdapter)
        pbLoadingIndicator1 = splAttendeeDialog.findViewById(R.id.pbLoadingIndicator)
        etSearchSplAttendeeText.setLoadingIndicator(pbLoadingIndicator)
        etSearchSplAttendeeText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                uiSubject.onNext(
                    IndirectSetAppointmentUiEvent.SearchTextChangeSplAttendee(
                        s?.toString() ?: ""
                    )
                )
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
        })
        etSearchSplAttendeeText.setOnItemClickListener { _, _, position, _ ->
            uiSubject.onNext(IndirectSetAppointmentUiEvent.SplAttendeeItemClick(position))
        }
        splAttendeeDialog.show()
    }

    override fun showSplAttendeAddedDialog() {
       // showAddedDialog(false)
    }

    override fun onSplAttendeeItemClickEventFired(): Observable<IndirectSetAppointmentUiEvent.SplAttendeeItemClick> =
        uiSubject.ofType()

    override fun getSplAttendeeSearchText(): Observable<IndirectSetAppointmentUiEvent.SearchTextChangeSplAttendee> {
        return uiSubject.ofType()
    }

    override fun renderOnSplAttendeeItemClick(position: Int) {
        val attendee = splAttendeeAdapter.getItem(position)
        if (!CreateMeetingConstants.hasAttendeeAdded(addedSplAttendeeList, attendee.empCode)) {
            addedSplAttendeeList.add(AddedAttendee(attendee.empName, attendee.empCode, "S"))
            addedSplAttendeeCount++
            setCount(addedSplAttendeeCount, binding.tvspAttendeeCount)
            splAttendeeDialog.dismiss()
        } else {
            splAttendeeDialog.dismiss()
            Toast.makeText(requireContext(), "Special Attendee Already Added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSaveButtonClicked(): Observable<Unit> {
        return  binding.btnSave.clicks()
    }

    override fun renderAddUpdates(uiModel: UiModel<SetAppointmentResponseData>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            MSFDialog.alertDialog(
                activity, 0,
                resources.getString(R.string.motilal_oswal),
                if (createMode) MOSLConstants.SET_APPOINTMENTS else MOSLConstants.UPDATE_APPOINTMENTS,
                "OK", true
            ) { action, _ ->
                if (action == MSFDialog.Action.OK) {
                    val bundle = Bundle()
                    bundle.putString(
                        MOSLConstants.LEAD_ID,
                        requireArguments().getString(MOSLConstants.LEAD_ID)
                    )
                    bundle.putString(
                        MOSLConstants.UCID,
                        requireArguments().getString(MOSLConstants.UCID)
                    )
                    navigator.openPage(NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_DETAIL_MAIN.toString(), bundle, true, false)
                }

            }

            onResentButtonRender()
        }
    }

    override fun onResetButtonClicked(): Observable<Unit> {
        return binding.btnReset.clicks()
    }

    override fun onResentButtonRender() {
        addedAttendeeList.clear()
        addedSplAttendeeList.clear()
        binding.tvAttendeeCount.text = ""
        binding.tvspAttendeeCount.text = ""
        binding.spActivityType.setSelection(0)
        binding.spmeetingReason.setSelection(0)
        binding.etMeetingDescription.setText("")

    }

    override fun getDate(): String {
        return getFormatedDate(binding.tvApppointmentDateTimeDisplay.text.toString(), AppDateFormats.DF_JSON_DATE12, AppDateFormats.DF_JSON_DATE9)
    }

    override fun getSaveData(): SetAppointmentPostReq {

            val meetingApptModel = MeetingApptModel().apply {
                meetingType = "A"
                displayType = "A"
                leadId =leadID
                ucid = UcId
                meetingDate = getDate()
                when {
                    !createMode -> meetingID = meetingId
                }
                meetingAttendee = addedAttendeeList.map {
                    AddedAttendeeModel(
                        it.attendeeType,
                        it.attended,
                        it.attendeeName,
                        it.attendeeCode
                    )
                }
                splMeetingAttendee = addedSplAttendeeList.map {
                    AddedSplAttendeeModel(
                        it.attendeeType,
                        it.attended,
                        it.attendeeName,
                        it.attendeeCode
                    )
                }
                product = "0"
                activityTypeID = getSelectedActivityType().lookUpID.toString()
                meetingReasonID = getSelectedMeetingReason().lookUpID
                meetingDes = binding.etMeetingDescription.text.toString()

            }
            var objSetAppointmentPostReq: SetAppointmentPostReq = SetAppointmentPostReq()
            objSetAppointmentPostReq.product = meetingApptModel.product

            var tempList: MutableList<MeetingAttendeeItem?> = addedAttendeeList.map {
                MeetingAttendeeItem(
                    it.attended,
                    it.attendeeName,
                    "0",
                    it.attendeeCode,
                    it.attendeeType
                )
            }.toMutableList()

            addedSplAttendeeList.forEach {
                tempList.add(
                    MeetingAttendeeItem(
                        it.attended,
                        it.attendeeName,
                        "0",
                        it.attendeeCode,
                        it.attendeeType
                    )
                )
            }

            objSetAppointmentPostReq.meetingAttendee = tempList

            objSetAppointmentPostReq.mEETINGTYPEMEETING = meetingApptModel.meetingType


            objSetAppointmentPostReq.meetingDurationID = meetingApptModel.meetingDurationID

            objSetAppointmentPostReq.meetingType = meetingApptModel.meetingType


            objSetAppointmentPostReq.meetingDes = meetingApptModel.meetingDes

            objSetAppointmentPostReq.mEETINGTYPEAPPT = "M"

            objSetAppointmentPostReq.displayType = meetingApptModel.displayType

            objSetAppointmentPostReq.dISPLAYTYPEMEETING = "U"

            objSetAppointmentPostReq.meetingObjectiveID = meetingApptModel.meetingObjectiveID

            objSetAppointmentPostReq.meetingDate = meetingApptModel.meetingDate

            objSetAppointmentPostReq.meetingReasonID = meetingApptModel.meetingReasonID

            objSetAppointmentPostReq.activityTypeID = meetingApptModel.activityTypeID

            objSetAppointmentPostReq.leadId = meetingApptModel.leadId

            objSetAppointmentPostReq.ucid = meetingApptModel.ucid

            objSetAppointmentPostReq.meetingId = meetingApptModel.meetingID

            return objSetAppointmentPostReq
        }

    override fun getMeetingById(): Observable<IndirectSetAppointmentUiEvent.GetMeeting> {
       return uiSubject.ofType()
    }
    override fun renderMeetingByID(uiModel: UiModel<MeetingByIdItemResponse>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            meetingApptModel = getAppointmentData(it)
            setCalenderDateFrom(meetingApptModel!!)
        }
    }

    private fun getAppointmentData(data: MeetingByIdItemResponse): MeetingApptModel {
        val mModel = MeetingApptModel().apply {
            meetingType = stringOrEmpty(data.meetingType)
            displayType = "A"
            meetingID = stringOrEmpty(data.meetingID)
            leadId = data.leadId.toString()
            meetingDate = stringOrEmpty(data.meetingDate)
            activityTypeID = stringOrEmpty(data.activityTypeID)
            meetingReasonID= Integer.parseInt(stringOrZero(data.meetingReasonID))
            product = stringOrEmpty(data.productTypeID)
            appointmentStartTime = stringOrEmpty(data.repeatationStartOn)
            appointmentEndTime = stringOrEmpty(data.repeatationEndsOn)
            repeatEveryWeekCode = stringOrEmpty(data.repeatEveryWeekCode)
            repeatEveryDayCode = stringOrEmpty(data.repeatEveryDayCode)
            repeatOnCode = stringOrEmpty(data.repeatOnCode)
            meetingDes = stringOrEmpty(data.meetingDescription)
        }
        binding.etMeetingDescription.setText(mModel.meetingDes)

        val meetingAttendee = data.meetingAttendee
        for (meetingAttendeeItem in meetingAttendee.indices) {
            val meetingAttendeeObj = meetingAttendee[meetingAttendeeItem]

            val attendeeType = meetingAttendeeObj.attendeeType
            val attendeeEmpCode = meetingAttendeeObj.attendeeEmpcode ?: ""
            val attendeeEmpName = meetingAttendeeObj.attendeeEmpName ?: ""
            val meetingAttendeeCode = meetingAttendeeObj.meetingAttendeeID ?: ""
            val attended = meetingAttendeeObj.attended

            when (meetingAttendeeObj.status) {
                1 -> if (!meetingAttendeeObj.defaultAttendee) {
                    when {
                        attendeeType!!.trim { it <= ' ' }.equals("R", ignoreCase = true) -> {
                            val attendTmp = AddedAttendeeModel(
                                attendeeType,
                                attended,
                                attendeeEmpName,
                                attendeeEmpCode,
                                meetingAttendeeCode
                            )
                            addedAttendeeList.add(AddedAttendee(attendTmp.attendeeName, attendTmp.attendeeEmplyeeCode, attendTmp.attendeeType))
                            addedAttendeeCount++
                        }
                        else -> {
                            val tempSpAttendee = AddedSplAttendeeModel(
                                attendeeType,
                                attended,
                                attendeeEmpName,
                                attendeeEmpCode
                            )
                            addedSplAttendeeList.add(AddedAttendee(tempSpAttendee.attendeeName, tempSpAttendee.attendeeEmplyeeCode, tempSpAttendee.attendeeType))
                            addedSplAttendeeCount++
                        }
                    }
                }
            }

        }

        binding.tvspAttendeeCount.text = String.format("(%s)", addedSplAttendeeCount.toString())
        setCount(addedAttendeeCount)
        mModel.meetingAttendee = addedAttendeeList .map {
            AddedAttendeeModel(it.attendeeType, it.attended, it.attendeeName, it.attendeeCode)
        }
        mModel.splMeetingAttendee = addedSplAttendeeList .map {
            AddedSplAttendeeModel(it.attendeeType, it.attended, it.attendeeName, it.attendeeCode)
        }
        return mModel
    }
    private fun setCount(cnt: Int) {
        binding.tvAttendeeCount.text = when {
            cnt <= 0 -> ""
            else -> String.format("(%s)", cnt.toString())
        }
    }
    private fun setCalenderDateFrom(mModel: MeetingApptModel) {

        binding.apply {
            var mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.meetingDate)
            tvApppointmentDateTime.text = CRMAppUtil.getDBDate(mMeetingDate,
                MOSLConstants.mFormatWithSpace
            )
            tvApppointmentDateTimeDisplay.text =
                CRMAppUtil.getDBDate(mMeetingDate, MOSLConstants.DATE_FORMAT_TO_DISPLAY)
            }
    }


}