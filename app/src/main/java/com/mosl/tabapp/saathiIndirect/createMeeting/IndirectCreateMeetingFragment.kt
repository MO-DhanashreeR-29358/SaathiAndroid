package com.mosl.tabapp.saathiIndirect.createMeeting

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
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
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.AddUpdateMeeting
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
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MoslConstant
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCreateMeetingBinding
import com.mosl.tabapp.geoLocation.LocationItem
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModeAdapter
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel
import com.mosl.tabapp.leadViewDetail.createmeeting.AttendeeAdapter
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.CreateMeetingPostReq
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.model.MeetingAttendeeItems
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import com.mosl.tabapp.geoLocation.GeoTaggingFragment
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentUiEvent

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
    private lateinit var intrestedInAdapter:ArrayAdapter<LookUp>
    private lateinit var mainFromDate: String
    var selectedDate : String = CRMAppUtil.getDateStr(Date(), MoslConstant.dd_MM_yyyy)
    private val uiSubject = PublishSubject.create<IndirectCreateMeetingUiEvent>()
    private lateinit var attendeeDialog: Dialog
    private lateinit var etSearchAttendeeText: DelayAutoCompleteTextView
    private lateinit var attendeeAdapter: AttendeeAdapter
    private var pbLoadingIndicator: ProgressBar? = null
    private val addedAttendeeList: ArrayList<AddedAttendee> = ArrayList()
    private var addedAttendeeCount = 0


    private lateinit var travellingAdapter: CreateMeetingTravellingModeAdapter
    var leadID:String=""
    var UcId:String=""
    var meetingId:String=""
    private var createMode = true
    private var meetingApptModel: MeetingApptModel? = null

    private var isAreaRestricted :Boolean = true
    var strLatitude : String = ""
    var strLongitude : String = ""
    var strMeetingLatitude : String = ""
    var strMeetingLongitude : String = ""
    var strAddress : String = ""
    var isMockLocationEnabled : Boolean = false
    override fun getLayout() = R.layout.frag_indirect_create_meeting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCreateMeetingBinding.bind(view)
        setAppTitle(R.string.page_title_create_meeting)
        hideAppTitle()
        presenter.start()
        setupViews()
        setUpFromArguments()
        dataActionSubject.onNext(DataAction.Fetch)

        val value =
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        if (value == PackageManager.PERMISSION_GRANTED) {
            // fusedLocation.initDispatcher(this)
            val dialog = GeoTaggingFragment()
            dialog.setIndirectData(uiSubject, true, userManager)
            dialog.show(activity?.supportFragmentManager!!, "UserAddress")

        }
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

        intrestedInAdapter=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spIntrestedIn.adapter=intrestedInAdapter

        mainFromDate = CRMAppUtil.getDate(Date(), MoslConstant.yyyy_MM_dd)
        binding.tvDate.text = CRMAppUtil.getDBDate(Date(), MOSLConstants.DATE_FORMAT_TO_DISPLAY)

        travellingAdapter = CreateMeetingTravellingModeAdapter(requireContext())
        binding.rvTravellingList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvTravellingList.adapter = travellingAdapter

        binding.etMeetingAdd.setOnClickListener {
            isAreaRestricted = false
            if(getSelectedMeetingLocation().lookUpName != "Branch") {
                val dialog = GeoTaggingFragment()
                dialog.setIndirectData(uiSubject, isAreaRestricted, userManager)
                dialog.show(activity?.supportFragmentManager!!, "UserAddress")
            }
        }

    }

    private fun setUpFromArguments() {
        try {
            leadID =this.arguments?.getString(MOSLConstants.LEAD_ID, "0") ?: "0"
            UcId =this.arguments?.getString(MOSLConstants.UCID, "0") ?: "0"
            meetingId = this.arguments?.getString(MOSLConstants.MEETING_ID, "0") ?: "0"
            if(!meetingId.equals("0", ignoreCase = true)) {
                //Editt mode
                createMode = false
                setAppTitle(R.string.page_title_update_meeting)
                uiSubject.onNext(IndirectCreateMeetingUiEvent.GetMeeting(meetingId!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun extractArguments(bundle: Bundle) {
        super.extractArguments(bundle)
        leadID =bundle.getString(MOSLConstants.LEAD_ID, "0") ?: "0"
        UcId =bundle.getString(MOSLConstants.UCID, "0") ?: "0"
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
        uiSubject.onNext(IndirectCreateMeetingUiEvent.onActivityCall)
    }

    override fun ononActivityCall(): Observable<IndirectCreateMeetingUiEvent.onActivityCall> {
        return  uiSubject.ofType()
    }
    override fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            activityAdapter.setSpinnerData(binding.spActivityType, it)
            val index = it.indexOfFirst { it.lookUpID.toString() == meetingApptModel?.activityTypeID }
            binding.spActivityType.setSelection(index)
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
            val index = it.indexOfFirst { it.lookUpID == meetingApptModel?.meetingReasonID }
            binding.spMeetingReason.setSelection(index)
            uiSubject.onNext(IndirectCreateMeetingUiEvent.OnIntrestedInCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onIntrestedInCall(): Observable<IndirectCreateMeetingUiEvent.OnIntrestedInCall> {
        return   uiSubject.ofType()
    }

    override fun fnRenderIntrestedIn(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            intrestedInAdapter.setSpinnerData(binding.spIntrestedIn,it)
            val index = it.indexOfFirst { it.lookUpID.toString() == meetingApptModel?.product }
            binding.spIntrestedIn.setSelection(index)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun fnGetSelectedIntrestedIn(): LookUp? {
        return getSelectedSpinnerValue(binding.spIntrestedIn)
    }

    override fun onMeetingDurationCall(): Observable<IndirectCreateMeetingUiEvent.OnMeetingDurationCall> {
        return uiSubject.ofType()
    }

    override fun fnRenderMeetingDuration(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            meetingDurationAdapter.setSpinnerData(binding.spMeetingDuration, it)
            val index = it.indexOfFirst { it.lookUpID == meetingApptModel?.meetingDurationID }
            binding.spMeetingDuration.setSelection(index)
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
            val index = it.indexOfFirst { it.lookUpID.toString() == meetingApptModel?.meetingObjectiveID }
            binding.spMeetingObjective.setSelection(index)
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
            val index = it.indexOfFirst { it.lookUpID.toString() == meetingApptModel?.meetingAddressTypeId }
            binding.spLocationAdd.setSelection(index)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
        uiSubject.onNext(IndirectCreateMeetingUiEvent.OnTravellingModeCall)
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
                uiSubject.onNext(IndirectCreateMeetingUiEvent.SearchTextChange(s?.toString() ?: ""))
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
            uiSubject.onNext(IndirectCreateMeetingUiEvent.AttendeeItemClick(position))
        }
        attendeeDialog.show()
    }

    override fun getAttendeeSearchText(): Observable<IndirectCreateMeetingUiEvent.SearchTextChange> {
        return  uiSubject.ofType()
    }

    override fun onAttendeeItemClickEventFired(): Observable<IndirectCreateMeetingUiEvent.AttendeeItemClick> {
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
            }
        }
    }

    override fun renderSearchResult(
        uiModel: UiModel<List<Attendee>>,
        isAttendee: Boolean
    ) {
        toggleSearchProgress(uiModel.inProgress, isAttendee)

        uiModel.onFailure {
            if (isAttendee) {
                attendeeAdapter.setErrorData(it)
            } else {

            }
        }

        uiModel.onSuccess {
            if (isAttendee) {
                attendeeAdapter.setResultList(it)
                if (it.isNotEmpty()) {
                    etSearchAttendeeText.showDropDown()
                }
            } else {

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
            }
        } else {
            if (isAttendee) {
                pbLoadingIndicator?.visibility = View.GONE
            } else {
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

    override fun getAttendeeList(): List<AddedAttendee> {
        return addedAttendeeList
    }

    override fun getPincode(): String {
        return binding.etAddPinCode.text.toString()
    }

    override fun getAmount(): String {
        return binding.etMeetingAmount.text.toString()
    }

    override fun getMeetingAddress(): String {
        return binding.etMeetingAdd.text.toString()
    }

    override fun getDate(): String {
        return getFormatedDate(binding.tvDate.text.toString(), AppDateFormats.DF_JSON_DATE12, AppDateFormats.DF_JSON_DATE9)
    }
    override fun getSelectedMeetingDuration(): LookUp? {
        return getSelectedSpinnerValue(binding.spMeetingDuration)
    }

    private fun getSelectedSpinnerValue(spinner: Spinner): LookUp? {
        if (spinner.selectedItem is LookUp) {
            return (spinner.selectedItem as LookUp)
        }
        return null
    }
    override fun getSelectedMeetingObjective(): LookUp? {
        return getSelectedSpinnerValue(binding.spMeetingObjective)
    }
    override fun getSelectedMeetingReason(): LookUp {
        return binding.spMeetingReason.selectedItem as LookUp
    }
    override fun getMeetingDesc(): String {
        return binding.etMeetingDescription.text.toString()
    }

    override fun getMeetingLatitude(): String? {
        return strMeetingLatitude
    }

    override fun getMeetingLongitude(): String? {
        return strMeetingLongitude
    }

    override fun onTravellingModeSelected(): Observable<Int> {
        return binding.spTravellingMode.itemSelections()
    }
    override fun getSelectedTravellingMode(): LookUp {
        return (binding.spTravellingMode.selectedItem as LookUp)
    }
    override fun renderOnTravellingModeClick(lookUp: LookUp) {
        travellingAdapter.addTravellingMode(
            CreateMeetingTravellingModel(lookUp.lookUpID.toString(), lookUp.lookUpName)
        )
    }
    override fun getTravellingmodeList(): List<CreateMeetingTravellingModel> {
        return travellingAdapter.travellingList
    }


    override fun onMeetingLocationSelected(): Observable<Int> {
        return binding.spLocationAdd.itemSelections()
    }
    override fun getSelectedMeetingLocation(): LookUp {
        return (binding.spLocationAdd.selectedItem as LookUp)
    }

    override fun renderAddress(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            binding.etMeetingAdd.setText("")
            Timber.d("CreateMeetingFragment renderAddress showOnFailurePopup ${it.message}")

            /* if((it.getErrorOnFailure().getResolvedMessage(requireContext())).length > 0) {
                 showOnFailurePopup(it)
             }*/
        }

        uiModel.onSuccess {
            if(getSelectedMeetingLocation().lookUpName == "Branch"){
                binding.etMeetingAdd.setText(it)
            }else{
                binding.etMeetingAdd.setText("")
            }

        }
    }
    override  fun getInputData(): CreateMeetingPostReq {
        val attendeeList = if (getAttendeeList()
                .isEmpty()
        ) {
            emptyList()
        } else getAttendeeList()
            .map {
                AddedAttendeeModel(it.attendeeType, it.attended, it.attendeeName, it.attendeeCode)
            }

        val data = MeetingApptModel()
        data.meetingType = "M"
        data.displayType = "A"
        data.pincode = getPincode()
        data.amount = getAmount()
        data.meetingAddress = getMeetingAddress()
        data.meetingLatitude = getMeetingLatitude()
        data.meetingLongitude = getMeetingLongitude()
        data.meetingAddressTypeId = getSelectedMeetingLocation()?.lookUpID.toString()
        data.leadId = leadID
        data.ucid = UcId
        when {
            !createMode -> data.meetingID = meetingId
        }
        data.meetingDate = getDate()
        data.product = "0"
        data.setProductList(emptyList())
        data.meetingDuration = getSelectedMeetingDuration()?.lookUpName
        data.meetingDurationID=getSelectedMeetingDuration()?.lookUpID?:0
        data.meetingObjectiveID = getSelectedMeetingObjective()?.lookUpID.toString()
        data.activityTypeID = getSelectedActivityType()?.lookUpID.toString()
        data.meetingReason = getSelectedMeetingReason()?.lookUpName
        data.meetingReasonID= getSelectedMeetingReason()?.lookUpID?:0
        data.meetingDes = CRMAppUtil.getJsonSafeString(getMeetingDesc())
        data.meetingAttendee = attendeeList
        data.splMeetingAttendee= emptyList()
        data.travellingList = getTravellingmodeList()


        data.geoArea = strAddress
        data.geoLatitude = strLatitude
        data.geoLongitude = strLongitude

        var objPostData: CreateMeetingPostReq=CreateMeetingPostReq()

        objPostData.meetingLatitude=data.meetingLatitude
        objPostData.meetingLongitude=data.meetingLongitude
        objPostData. meetingAddress=data.meetingAddress
        objPostData. mEETINGTYPEMEETING="M"
        objPostData. meetingType="M"
        objPostData. meetingDuration=data.meetingDuration

        var strTavelling=""
        if(data.travellingList.size!=0)
        {
            for (model in data.travellingList) {
                val meetingJson = model.travellingId
                strTavelling += "$meetingJson,"
            }

            if (!strTavelling.isEmpty()) {
                strTavelling = strTavelling.substring(0, strTavelling.length - 1) //remove lash comma
            }
        }
        objPostData. travellingList=strTavelling
        objPostData. meetingDate =data.meetingDate
        objPostData. meetingDescription=data.meetingDes
        objPostData. leadId=data.leadId
        objPostData. meetingId = data.meetingID
        objPostData. meetingReason=data.meetingReason
        objPostData. ucid=data.ucid
        objPostData. pincode=data.pincode
        objPostData. amount=data.amount
        objPostData. product=data.product
        var tempList :MutableList<MeetingAttendeeItems?> =getAttendeeList().map {
            MeetingAttendeeItems(it.attended,it.attendeeName,"0",it.attendeeCode,it.attendeeType)
        }.toMutableList()
        objPostData. meetingAttendee=tempList
        objPostData. meetingLongitude =data.meetingLongitude
        objPostData. meetingDurationID=data.meetingDurationID
        objPostData. geoLongitude=data.geoLongitude
        objPostData. geoArea=data.geoArea
        objPostData. mEETINGTYPEAPPT ="A"
        objPostData. displayType="A"
        objPostData. geoLatitude=data.geoLatitude
        objPostData. dISPLAYTYPEMEETING="U"
        objPostData. meetingAddressTypeId=data.meetingAddressTypeId
        objPostData. meetingObjectiveID=data.meetingObjectiveID
        objPostData. meetingReasonID=data.meetingReasonID
        objPostData. activityTypeID=data.activityTypeID
        objPostData.productTypeID=fnGetSelectedIntrestedIn()?.lookUpID.toString()
        var aId=""
        if(tempList.size!=0)
        {
            for (model in tempList) {
                val meetingJson = model?.attendeeEmplyeeCode
                aId += "$meetingJson,"
            }

            if (!aId.isEmpty()) {
                aId = aId.substring(0, aId.length - 1) //remove lash comma
            }
        }
        objPostData.attendeeCode=aId

        return objPostData
    }

    override fun onResetClicked(): Observable<Unit> {
        return  binding.btnReset.clicks()
    }

    override fun renderOnResetClicked() {
        addedAttendeeList.clear()
        binding.tvAttendeeCount.text = ""
        binding.spActivityType.setSelection(0)
        binding.spMeetingReason.setSelection(0)
        binding.spIntrestedIn.setSelection(0)
        binding.spMeetingDuration.setSelection(0)
        binding.etMeetingDescription.setText("")
        binding.spMeetingObjective.setSelection(0)
        binding.etCustRemark.setText("")
        binding.spLocationAdd.setSelection(0)
        binding.etMeetingAdd.setText("")
        binding.spTravellingMode.setSelection(0)
        travellingAdapter.deleteAllProduct()
        binding.etAddPinCode.setText("")
        binding.etMeetingAmount.setText("")


    }

    override fun onSaveClicked(): Observable<Unit> {
        return  binding.btnSave.clicks()
    }

    override fun renderSaveRequest(uiModel: UiModel<AddUpdateMeeting>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("IndirectCreateMeetingFragment renderSaveRequest showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            MSFDialog.alertDialog(
                requireContext(), 0, resources.getString(R.string.motilal_oswal),
                MOSLConstants.CREATE_MEETING, "OK", true
            )
            { action: MSFDialog.Action, _: Array<Any?>? ->
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
                    navigator.openPage(
                        NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_DETAIL_MAIN.toString(), bundle,
                        addToStack = true,
                        forceReload = false
                    )
                }
            }
            renderOnResetClicked()
        }


    }

    override fun onIndirectLocationSaveEventFired(): Observable<IndirectCreateMeetingUiEvent.OnIndirectLocationSaveClick> {
        return uiSubject.ofType()
    }

    override fun renderLocationSave(locationItem: LocationItem) {
        if(isAreaRestricted) {
            strLatitude = locationItem.latitude
            strLongitude = locationItem.longitude
            strAddress = locationItem.address
            isMockLocationEnabled = locationItem.isMockLocationEnabled
        }else
        {
            strMeetingLatitude = locationItem.latitude
            strMeetingLongitude = locationItem.longitude
            binding.etMeetingAdd.setText(locationItem.address)
        }

        Log.d("strLatitude",strLatitude)
        Log.d("strLongitude",strLongitude)
        Log.d("strAddress",strAddress)

        Log.d("strMeetingLatitude",strMeetingLatitude)
        Log.d("strMeetingLongitude",strMeetingLongitude)
        Log.d("strMeetingAddress",binding.etMeetingAdd.text.toString())
        //Toast.makeText(requireContext(),"CreateMeeting - " + strLatitude+ "-"+ strLongitude + "-" + strAddress + "-"+ isMockLocationEnabled.toString(),Toast.LENGTH_LONG).show()

    }

    override fun getMeetingById(): Observable<IndirectCreateMeetingUiEvent.GetMeeting> {
        return  uiSubject.ofType()
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
            meetingDurationID = Integer.parseInt(stringOrZero(data.meetingDurationID))
            meetingObjectiveID = stringOrEmpty(data.meetingObjectiveID.toString())
            meetingAddressTypeId = stringOrEmpty(data.meetingAddressTypeId.toString())
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

                    }
                }
            }

        }

        binding.tvAttendeeCount.text = String.format("(%s)", addedAttendeeCount.toString())
        setCount(addedAttendeeCount, binding.tvAttendeeCount)
        mModel.meetingAttendee = addedAttendeeList .map {
            AddedAttendeeModel(it.attendeeType, it.attended, it.attendeeName, it.attendeeCode)
        }

        return mModel
    }

    private fun setCalenderDateFrom(mModel: MeetingApptModel) {

        binding.apply {
            var mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.meetingDate)
            binding.tvDate.text =
                CRMAppUtil.getDBDate(mMeetingDate,
                    MOSLConstants.DATE_FORMAT_TO_DISPLAY
                )

        }
    }

}