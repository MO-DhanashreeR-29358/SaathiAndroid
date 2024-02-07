package com.mosl.tabapp.leadViewDetail.createmeeting

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.*
import com.mosl.mobile.user.login.user
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE12
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE9
import com.mosl.shared.util.getFormatedDate
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.R.string
import com.mosl.tabapp.bankmeeting.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMMOSLCommon
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.constants.MoslConstant.dd_MMM_yyyy_hh_mm
import com.mosl.tabapp.constants.MoslConstant.dd_MM_yyyy
import com.mosl.tabapp.constants.MoslConstant.yyyy_MM_dd
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragCreateMeetingBinding
import com.mosl.tabapp.leadViewDetail.CreateMeetingProductAdapter
import com.mosl.tabapp.leadViewDetail.CreateMeetingProductModel
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModeAdapter
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_ATLEAST_FIFTY_CHARACTERS
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_ATLEAST_TEN_CHARACTERS
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_ATLEAST_TWENTY_CHARACTERS
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_INVALID_PINCODE_NUMBER
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_LOCATION_CANNOT_BE_LEFT_BLANK
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_ACTIONABLE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_ACTIVITY_TYPE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_FEEDBACK
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_DURATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_LOCATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_OBJECTIVE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_REASON
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MOBILE_NUMBER
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_PRODUCT
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_SUB_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_TOUCHED_UNTOUCHED
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_TRAVELLING_MODE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_ACTIONABLE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_FEEDBACK
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_DURATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_LOCATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_OBJECTIVE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_REASON
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_PRODUCT
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_SUB_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_TOUCHED_UNTOUCHED
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_TRAVELLING_MODE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.hasAttendeeAdded
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.*
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.gone
import com.mosl.tabapp.util.setErrorMessages
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.mosl.tabapp.geoLocation.GeoTaggingFragment
import com.mosl.tabapp.geoLocation.LocationItem
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_EVENT


class CreateMeetingFragment : CommonFragment(), CreateMeetingView, OTPFragment.OTPDialogListener,
     AddedAttendeeAdapter.ClickListenerHandler {
    lateinit var binding: FragCreateMeetingBinding
    private lateinit var attendeeAdapter: AttendeeAdapter
    private lateinit var addedAttendeeAdapter: AddedAttendeeAdapter
    private lateinit var splAttendeeAdapter: AttendeeAdapter
    private lateinit var addedSplAttendeeAdapter: AddedAttendeeAdapter

    @Inject
    lateinit var presenter: CreateMeetingPresenter

    @Inject
    lateinit var permissionCheckHelper: PermissionCheckHelperFragment

    /* @Inject
     lateinit var fusedLocation: FusedLocation*/

    private lateinit var productAdapter: ArrayAdapter<LookUp>
    private lateinit var categoryAdapter: ArrayAdapter<LookUp>
    private lateinit var subCategoryAdapter: ArrayAdapter<LookUp>
    private lateinit var actionableAdapter: ArrayAdapter<LookUp>
    private lateinit var activityAdapter: ArrayAdapter<LookUp>
    private lateinit var mobileNumberAdapter: ArrayAdapter<MobileListData>
    private lateinit var meetingObjectiveAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingDurationAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingLocationAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingReasonAdapter: ArrayAdapter<LookUp>
    private lateinit var touchedUntouchedAdapter: ArrayAdapter<LookUp>
    private lateinit var feedbackAdapter: ArrayAdapter<LookUp>
    private lateinit var travellingModeAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingProductAdapter: CreateMeetingProductAdapter
    private lateinit var travellingAdapter: CreateMeetingTravellingModeAdapter
    private lateinit var chipsAdapter: ArrayAdapter<String>

    private val uiSubject = PublishSubject.create<CreateMeetingUiEvent>()
    private lateinit var mainFromDate: String
    override fun getLayout() = layout.frag_create_meeting

    private val PERMISSIONS_REQUEST_ENABLE_GPS: Int = 1005
    private val ERROR_DIALOG_REQUEST: Int = 203
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1004
    var mLocationPermissionGranted = false
    var isLocEn : Boolean? = null
    var currentDate : String = CRMAppUtil.getDateStr(Date(), dd_MM_yyyy)
    var selectedDate : String = CRMAppUtil.getDateStr(Date(), dd_MM_yyyy)
    var flgReactivationClient : String  = "0"

    var strLatitude : String = ""
    var strLongitude : String = ""
    var strAddress : String = ""
    var isMockLocationEnabled : Boolean = false
    var strSourceName : String = ""
    var strProspectSource : String = ""
    var strActivateOTPInApp : String = ""

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragCreateMeetingBinding.bind(view)
        setAppTitle(string.page_title_create_meeting)
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)

        if (permissionCheckHelper.locationPermission()) {
            // fusedLocation.initDispatcher(this)
            val dialog = GeoTaggingFragment()
            dialog.setData(uiSubject)
            dialog.show(activity?.supportFragmentManager!!, "UserAddress")

        }
    }

    private fun setupViews() {
        meetingProductAdapter = CreateMeetingProductAdapter(requireContext())
        travellingAdapter = CreateMeetingTravellingModeAdapter(requireContext())
        productAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spProduct.adapter = productAdapter

        activityAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActivityType.adapter = activityAdapter

        chipsAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spChips.adapter = chipsAdapter

        mobileNumberAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMobileNumber.adapter = mobileNumberAdapter

        meetingLocationAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingLocation.adapter = meetingLocationAdapter

        meetingDurationAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingDuration.adapter = meetingDurationAdapter

        meetingObjectiveAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingObjective.adapter = meetingObjectiveAdapter

        travellingModeAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spTravellingMode.adapter = travellingModeAdapter

        meetingReasonAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spMeetingReason.adapter = meetingReasonAdapter

        touchedUntouchedAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spTouchedUntouched.adapter = touchedUntouchedAdapter

        feedbackAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFeedback.adapter = feedbackAdapter

        categoryAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spCategory.adapter = categoryAdapter

        actionableAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActionables.adapter = actionableAdapter

        subCategoryAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spSubCategory.adapter = subCategoryAdapter


        binding.rvProductList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvProductList.adapter = meetingProductAdapter

        binding.rvTravellingList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvTravellingList.adapter = travellingAdapter
        AddedAttendeeModel.setAttendeeUserEmplyeeCode(userManager.getUser()?.employeeCode)

    }

    override fun extractArguments(bundle: Bundle) {
        super.extractArguments(bundle)
        flgReactivationClient = bundle.getString(MOSLConstants.FLG_REACTIVATION_CLIENT, "0") ?: "0"
    }

    override fun onDataSaved(): Observable<SaveRequestData> {
        return uiSubject.ofType()
    }

    override fun onSaveConfirmClick(): Observable<SaveConfirmClick> {
        return uiSubject.ofType()
    }

    override fun onLocationSaveEventFired(): Observable<OnLocationSaveClick> {
        return uiSubject.ofType()
    }

    override fun renderLocationSave(locationItem: LocationItem) {
        strLatitude = locationItem.latitude
        strLongitude = locationItem.longitude
        strAddress = locationItem.address
        isMockLocationEnabled = locationItem.isMockLocationEnabled
        //Toast.makeText(requireContext(),"CreateMeeting - " + strLatitude+ "-"+ strLongitude + "-" + strAddress + "-"+ isMockLocationEnabled.toString(),Toast.LENGTH_LONG).show()


    }

    override fun onChipsApiCall(): Observable<CallChipsApi> {
        return uiSubject.ofType()
    }

    override fun renderChipsList(uiModel: UiModel<List<String>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderChipsList showOnFailurePopup ${it.message}")

            showOnFailurePopup(it) }

        uiModel.onSuccess {
            chipsAdapter.setSpinnerData(binding.spChips, it)
        }
    }

    override fun onTouchedUntouchedEventFired(): Observable<TouchedUntouchedRequest> {
        return uiSubject.ofType()
    }

    override fun renderTouchedUntouchedList(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderTouchedUntouchedList showOnFailurePopup ${it.message}")

            showOnFailurePopup(it) }

        uiModel.onSuccess {
            touchedUntouchedAdapter.setSpinnerData(binding.spTouchedUntouched, it)
            uiSubject.onNext(CreateMeetingUiEvent.FeedbackRequest)
        }
    }

    override fun onFeedbackEventFired(): Observable<FeedbackRequest> {
        return uiSubject.ofType()
    }

    override fun renderFeedbackList(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderFeedbackList showOnFailurePopup ${it.message}")

            showOnFailurePopup(it) }

        uiModel.onSuccess {
            feedbackAdapter.setSpinnerData(binding.spFeedback, it)
        }
    }

    override fun render(uiModel: UiModel<ClientData>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment render showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            val clientDetail = it.clientDetail
            strActivateOTPInApp = it.clientDetail.activateOTPInApp
            strSourceName = clientDetail.sourceName
            strProspectSource = clientDetail.prospectSource
            binding.layoutTopSection.tvLeadId.text = clientDetail.leadId.toString()
            binding.layoutTopSection.tvCurrentStage.text = clientDetail.currentStage
            if (clientDetail.currentStageSince.isNotEmpty()) {
                binding.layoutTopSection.tvCurrentStageSince.text =
                    String.format("%s Days", clientDetail.currentStageSince)
            }
            binding.layoutTopSection.tvName.text = clientDetail.name
            binding.layoutTopSection.tvClientCode.text = clientDetail.clientcode
            binding.layoutTopSection.tvBranchCode.text = clientDetail.branchcode
            val imageUrl = Base64.decode(it.imageUrl, Base64.DEFAULT)
            if (imageUrl != null) {
                CRMAppUtil.setImage(imageUrl, binding.layoutTopSection.ivLead, requireContext())
            }
            binding.tvDate.text = CRMAppUtil.getDBDate(Date(), DATE_FORMAT_TO_DISPLAY)
            productAdapter.setSpinnerData(binding.spProduct, it.listInterestedIn)
            meetingObjectiveAdapter.setSpinnerData(
                binding.spMeetingObjective,
                it.listMeetingObjective
            )
            meetingLocationAdapter.setSpinnerData(binding.spMeetingLocation, it.listMeetingAdressID)
            activityAdapter.setSpinnerData(binding.spActivityType, it.listActivity)
            mobileNumberAdapter.setSpinnerData(binding.spMobileNumber, it.listMobileNumber)
            meetingDurationAdapter.setSpinnerData(binding.spMeetingDuration, it.listMeetingDuration)
            travellingModeAdapter.setSpinnerData(binding.spTravellingMode, it.listTravellingMode)
            isOnlyClient = clientDetail.isOnlyClient
            isPotentialPMSProduct = clientDetail.isPotentialPMSProduct.isEmpty()
            mainFromDate = CRMAppUtil.getDate(Date(), yyyy_MM_dd)
            if(flgReactivationClient.equals("1")) {
                binding.tilTouchedUntouched.visibility = View.VISIBLE
                binding.tilFeedback.visibility = View.VISIBLE
                uiSubject.onNext(CreateMeetingUiEvent.TouchedUntouchedRequest)
            }else{
                binding.tilTouchedUntouched.visibility = View.GONE
                binding.tilFeedback.visibility = View.GONE
            }
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
            setContentView(layout.date_picker)
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
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
                val fromDat = CRMAppUtil.getDate(mainFromDate, yyyy_MM_dd)
                val fromPostingDate = CRMAppUtil.getDateStr(fromDat, dd_MM_yyyy)
                selectedDate = fromPostingDate
                val finalSelectedDateStr =
                    String.format(
                        "%s %d:%d:00", fromPostingDate, hour,
                        minute
                    ) //dd-MM-yyyy hh:mm:ss
                val finalSelectedDate =
                    CRMAppUtil.getDate(finalSelectedDateStr, dd_MMM_yyyy_hh_mm)
                if (CRMAppUtil.isBeforeandEqualDate(
                        finalSelectedDate
                    )
                ) { //
                    binding.tvDate.text =
                        CRMAppUtil.getDBDate(finalSelectedDate, DATE_FORMAT_TO_DISPLAY)

                    dialog.dismiss()
                } else {
                    MSFDialog.alertDialog(
                        requireContext(), 0,
                        resources.getString(string.motilal_oswal), MOSLConstants.INVALID_TIME,
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

    override fun renderAddress(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            binding.etLocation.setText("")
            Timber.d("CreateMeetingFragment renderAddress showOnFailurePopup ${it.message}")

            if((it.getErrorOnFailure().getResolvedMessage(requireContext())).length > 0) {
                showOnFailurePopup(it)
            }
        }

        uiModel.onSuccess {
            binding.etLocation.setText(it)
        }
    }

    override fun renderMeetingReason(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderMeetingReason showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            enableLocationGroup =
                binding.spActivityType.selectedItem.toString().equals("Personal Meeting", true)
            meetingReasonAdapter.clear()
            meetingReasonAdapter.setSpinnerData(binding.spMeetingReason, it)
        }
    }

    override fun renderSubCategory(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderSubCategory showOnFailurePopup ${it.message}")

            showOnFailurePopup(it) }

        uiModel.onSuccess {
            if (it.size > 1) {
                binding.llSubcategory.visibility = View.VISIBLE
                subCategoryAdapter.clear()
                subCategoryAdapter.setSpinnerData(binding.spSubCategory, it)
            } else {
                binding.llSubcategory.visibility = View.GONE
                binding.etSubCategory.setText("")
            }
        }
    }

    override fun renderCustomerServiceData(uiModel: UiModel<Pair<List<LookUp>, List<LookUp>>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderCustomerServiceData showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.llActionables.visibility = View.VISIBLE
            binding.llSubcategory.visibility = View.GONE
            actionableAdapter.setSpinnerData(binding.spActionables, it.second)
            categoryAdapter.setSpinnerData(binding.spCategory, it.first)
        }
    }

    private lateinit var etSearchAttendeeText: DelayAutoCompleteTextView
    private lateinit var etSearchSplAttendeeText: DelayAutoCompleteTextView
    private var pbLoadingIndicator: ProgressBar? = null
    private var pbLoadingIndicator1: ProgressBar? = null

    override fun getAttendeeSearchText(): Observable<SearchTextChange> {
        return uiSubject.ofType()
    }

    override fun getDate(): String {
        return getFormatedDate(binding.tvDate.text.toString(), DF_JSON_DATE12, DF_JSON_DATE9)
    }

    override fun getSplAttendeeSearchText(): Observable<SearchTextChangeSplAttendee> {
        return uiSubject.ofType()
    }

    private lateinit var attendeeDialog: Dialog
    private lateinit var splAttendeeDialog: Dialog
    override fun showAttendeDialog() {
        attendeeDialog = Dialog(requireContext())
        attendeeDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_attendee_list)
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
                uiSubject.onNext(SearchTextChange(s?.toString() ?: ""))
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
            uiSubject.onNext(AttendeeItemClick(position))
        }
        attendeeDialog.show()
    }

    override fun showSplAttendeDialog() {
        splAttendeeDialog = Dialog(requireContext())
        splAttendeeDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_attendee_list)
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
                uiSubject.onNext(SearchTextChangeSplAttendee(s?.toString() ?: ""))
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
            uiSubject.onNext(SplAttendeeItemClick(position))
        }
        splAttendeeDialog.show()
    }

    override fun onAttendeeItemClickEventFired(): Observable<AttendeeItemClick> = uiSubject.ofType()
    override fun onSplAttendeeItemClickEventFired(): Observable<SplAttendeeItemClick> =
        uiSubject.ofType()

    private var addedAttendeeCount = 0
    private var addedSplAttendeeCount = 0
    private val addedAttendeeList: ArrayList<AddedAttendee> = ArrayList()
    private val addedSplAttendeeList: ArrayList<AddedAttendee> = ArrayList()
    override fun renderOnAttendeeItemClick(position: Int) {
        val attendee = attendeeAdapter.getItem(position)
        if (!hasAttendeeAdded(addedAttendeeList, attendee.empCode)) {
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

    override fun renderOnSplAttendeeItemClick(position: Int) {
        val attendee = splAttendeeAdapter.getItem(position)
        if (!hasAttendeeAdded(addedSplAttendeeList, attendee.empCode)) {
            addedSplAttendeeList.add(AddedAttendee(attendee.empName, attendee.empCode, "S"))
            addedSplAttendeeCount++
            setCount(addedSplAttendeeCount, binding.tvSplAttendeeCount)
            splAttendeeDialog.dismiss()
        } else {
            splAttendeeDialog.dismiss()
            Toast.makeText(requireContext(), "Special Attendee Already Added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun renderOnProductClick(lookUp: LookUp) {
        meetingProductAdapter.addProduct(
            CreateMeetingProductModel(lookUp.lookUpID.toString(), lookUp.lookUpName)
        )
    }

    override fun renderOnTravellingModeClick(lookUp: LookUp) {
        travellingAdapter.addTravellingMode(
            CreateMeetingTravellingModel(lookUp.lookUpID.toString(), lookUp.lookUpName)
        )
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
            if (isAttendee) {
                attendeeAdapter.setErrorData(it)
            } else {
                splAttendeeAdapter.setErrorData(it)
            }
        }

        uiModel.onSuccess {
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

    private fun showAddedDialog(isAttendee: Boolean) {
        val dialogAddedAttendee = Dialog(requireContext())
        dialogAddedAttendee.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_addedd_attendee_list)
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
        }
        val tvEmpty = dialogAddedAttendee.findViewById<TextView>(R.id.tvEmpty)
        val lvAttendee = dialogAddedAttendee.findViewById<ListView>(R.id.lvAttendee)
        lvAttendee.emptyView = tvEmpty
        if (isAttendee) {
            addedAttendeeAdapter = AddedAttendeeAdapter(
                requireContext(), addedAttendeeList, this
            )
            lvAttendee.adapter = addedAttendeeAdapter
            addedAttendeeAdapter.notifyDataSetChanged()
        } else {
            addedSplAttendeeAdapter = AddedAttendeeAdapter(
                requireContext(), addedSplAttendeeList, this
            )
            lvAttendee.adapter = addedSplAttendeeAdapter
            addedSplAttendeeAdapter.notifyDataSetChanged()
        }
        dialogAddedAttendee.show()
    }

    override fun showAttendeAddedDialog() {
        showAddedDialog(true)
    }

    override fun onDeleteClicked(
        position: Int,
        isAttendee: Boolean
    ) {
        if (isAttendee) {
            addedAttendeeCount--
            setCount(addedAttendeeCount, binding.tvAttendeeCount)
            addedAttendeeList.removeAt(position)
            addedAttendeeAdapter.notifyDataSetChanged()
        } else {
            addedSplAttendeeCount--
            setCount(addedSplAttendeeCount, binding.tvSplAttendeeCount)
            addedSplAttendeeList.removeAt(position)
            addedSplAttendeeAdapter.notifyDataSetChanged()
        }
    }

    override fun showSplAttendeAddedDialog() {
        showAddedDialog(false)
    }

    //var location: Location? = null

    override fun onSendOTPClicked(): Observable<Unit> {
        return binding.btnSendOtp.clicks()
    }

    override fun getOTPButtonText(): String {
        return binding.btnSendOtp.text.toString()
    }

    override fun getLatitude(): String {
        return  strLatitude
    }

    override fun getLongitude(): String {
        return  strLongitude
    }

    override fun getAttendeeList(): List<AddedAttendee> {
        return addedAttendeeList
    }

    override fun getSplAttendeeList(): List<AddedAttendee> {
        return addedSplAttendeeList
    }

    override fun getTravellingmodeList(): List<CreateMeetingTravellingModel> {
        return travellingAdapter.travellingList
    }

    override fun getproductList(): List<CreateMeetingProductModel> {
        return meetingProductAdapter.listProduct
    }

    override fun getGeoArea(): String {
        var geoArea = ""
        /*CoroutineScope(Dispatchers.IO).launch {
            if (enableLocationGroup) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                location?.apply {
                    val addressLine = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addressLine.size > 0) {
                        geoArea = addressLine[0].getAddressLine(0)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                println("Geo area found ::: $geoArea")
            }
        }*/

            geoArea = strAddress
            /*val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                location?.apply {
                    val addressLine = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addressLine.size > 0) {
                        geoArea = addressLine[0].getAddressLine(0)
                    }
                }
            }catch (ex : Exception){
                ex.printStackTrace()
            }*/

        println("Geo area found ::: $geoArea")
        return geoArea
    }

    override fun onSaveClicked(): Observable<Unit> {
        return binding.btnSave.clicks()
    }

    override fun onResetClicked(): Observable<Unit> {
        return binding.btnReset.clicks()
    }

     fun resetDataOnActivityTypeChange() {
        errorDisabled()
        addedAttendeeList.clear()
        addedSplAttendeeList.clear()
        binding.tvAttendeeCount.text = ""
        binding.tvSplAttendeeCount.text = ""
        binding.spProduct.setSelection(0)
        binding.spMeetingDuration.setSelection(0)
        binding.spMeetingReason.setSelection(0)
        binding.spMeetingObjective.setSelection(0)
        binding.spMeetingLocation.setSelection(0)
        binding.spTravellingMode.setSelection(0)
        binding.spActionables.setSelection(0)
        binding.spCategory.setSelection(0)
        binding.spSubCategory.setSelection(0)
        binding.etCategory.setText("")
        binding.etSubCategory.setText("")
        binding.etLocation.setText("")
        binding.etPinCode.setText("")
        binding.etAmount.setText("")
        binding.etMeetingDescription.setText("")
        binding.etCustomerComment.setText("")
        binding.btnSendOtp.text = SEND_OTP_LABEL
        isSendOTPSuccesssful = false
        meetingProductAdapter.deleteAllProduct()
        travellingAdapter.deleteAllProduct()
    }


    override fun resetData() {
        errorDisabled()
        addedAttendeeList.clear()
        addedSplAttendeeList.clear()
        binding.tvAttendeeCount.text = ""
        binding.tvSplAttendeeCount.text = ""
        binding.spProduct.setSelection(0)
        binding.spMeetingDuration.setSelection(0)
        binding.spActivityType.setSelection(0)
        binding.spMobileNumber.setSelection(0)
        binding.spMeetingReason.setSelection(0)
        binding.spMeetingObjective.setSelection(0)
        binding.spMeetingLocation.setSelection(0)
        binding.spTravellingMode.setSelection(0)
        binding.spActionables.setSelection(0)
        binding.spCategory.setSelection(0)
        binding.spSubCategory.setSelection(0)
        binding.etCategory.setText("")
        binding.etSubCategory.setText("")
        binding.etLocation.setText("")
        binding.etPinCode.setText("")
        binding.etAmount.setText("")
        binding.etMeetingDescription.setText("")
        binding.etCustomerComment.setText("")
        binding.btnSendOtp.text = SEND_OTP_LABEL
        isSendOTPSuccesssful = false
        toggleOTPVisibility(false)
        meetingProductAdapter.deleteAllProduct()
        travellingAdapter.deleteAllProduct()
    }

    private fun errorDisabled() {
        binding.tilActivityType.disableErrorMessages()
        binding.tilProduct.disableErrorMessages()
        binding.tilMeetingDuration.disableErrorMessages()
        binding.tilMeetingObjective.disableErrorMessages()
        binding.tilMeetingReason.disableErrorMessages()
        binding.tilMeetingDescription.disableErrorMessages()
        binding.tilLocation.disableErrorMessages()
        binding.tilTravellingMode.disableErrorMessages()
        binding.tilLocation.disableErrorMessages()
        binding.tilCategory.disableErrorMessages()
        binding.tilSubCategory.disableErrorMessages()
        binding.tilActionables.disableErrorMessages()
        binding.tilPinCode.disableErrorMessages()
        binding.tilEtCategory.disableErrorMessages()
        binding.tilEtSubCategory.disableErrorMessages()
        binding.tilMeetingLocation.disableErrorMessages()
    }

    override fun validateFormData(): Boolean {
        errorDisabled()
        val status: Boolean = if (userManager.user.isRM() && !isSendOTPSuccesssful &&
            getSelectedActivityType()?.isPersonalMeeting() == true
        ) {
            alert("Please verify OTP to create meeting")
            false
        } else if (getSelectedActivityType() == CreateMeetingConstants.SELECT_ACTIVITY_TYPE) {
            binding.tilActivityType.setErrorMessages(MSG_SELECT_ACTIVITY_TYPE)
            false
        } else  if(userManager.getUser()?.EventListRequired == true && strSourceName == "Manual" && isBTLProspectSource() && getSelectedEvent().equals("Select Event")){
            binding.tilChips.setErrorMessages(MSG_SELECT_EVENT)
            false
        } else if (getSelectedActivityType()?.isPersonalMeeting() == true && !currentDate.equals(selectedDate)) {
            alert("Can not set previous date for Activity type Personal meeting")
            false
        } else if (getSelectedSpinnerValue(binding.spProduct) == SELECT_PRODUCT && meetingProductAdapter.listProduct.size == 0) {
            binding.tilProduct.setErrorMessages(MSG_SELECT_PRODUCT)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingDuration) == SELECT_MEETING_DURATION
        ) {
            binding.tilMeetingDuration.setErrorMessages(MSG_SELECT_MEETING_DURATION)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingObjective) == SELECT_MEETING_OBJECTIVE
        ) {
            binding.tilMeetingObjective.setErrorMessages(MSG_SELECT_MEETING_OBJECTIVE)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingReason) == SELECT_MEETING_REASON) {
            binding.tilMeetingReason.setErrorMessages(MSG_SELECT_MEETING_REASON)
            false
        } else if (binding.etMeetingDescription.text!!.trim().isEmpty()) {
            binding.tilMeetingDescription.setErrorMessages(
                MSG_MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK
            )
            false
        } else if (binding.etMeetingDescription.text!!.length < 50) {
            binding.tilMeetingDescription.setErrorMessages(MSG_ATLEAST_FIFTY_CHARACTERS)
            false
        } else if (enableLocationGroup && getSelectedSpinnerValue(binding.spMeetingLocation) == SELECT_MEETING_LOCATION
        ) {
            binding.tilMeetingLocation.setErrorMessages(MSG_SELECT_MEETING_LOCATION)
            false
        } else if (enableLocationGroup && getSelectedSpinnerValue(binding.spTravellingMode) == SELECT_TRAVELLING_MODE && travellingAdapter.travellingList.size == 0
        ) {
            binding.tilTravellingMode.setErrorMessages(MSG_SELECT_TRAVELLING_MODE)
            false
        } else if (enableLocationGroup && binding.etLocation.text!!.trim().isEmpty()) {
            binding.tilLocation.setErrorMessages(MSG_LOCATION_CANNOT_BE_LEFT_BLANK)
            false
        } else if (enableLocationGroup && binding.etLocation.text!!.length < 20) {
            binding.tilLocation.setErrorMessages(MSG_ATLEAST_TWENTY_CHARACTERS)
            false
        } else if (enableLocationGroup && binding.etPinCode.text!!.length < 6) {
            binding.tilPinCode.setErrorMessages(MSG_INVALID_PINCODE_NUMBER)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spActionables
            ) == SELECT_ACTIONABLE
        ) {
            binding.tilActionables.setErrorMessages(MSG_SELECT_ACTIONABLE)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spCategory
            ) == SELECT_CATEGORY
        ) {
            binding.tilCategory.setErrorMessages(MSG_SELECT_CATEGORY)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && binding.etCategory.text!!.length <= 10) {
            binding.tilEtCategory.setErrorMessages(MSG_ATLEAST_TEN_CHARACTERS)
            false
        } else if (binding.llSubcategory.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spSubCategory
            ) == SELECT_SUB_CATEGORY
        ) {
            binding.tilSubCategory.setErrorMessages(MSG_SELECT_SUB_CATEGORY)
            false
        } else if (binding.llSubcategory.visibility == View.VISIBLE && binding.etSubCategory.text!!.length <= 10) {
            binding.tilEtSubCategory.setErrorMessages(MSG_ATLEAST_TEN_CHARACTERS)
            false
        }else if (binding.tilTouchedUntouched.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spTouchedUntouched
            ) == SELECT_TOUCHED_UNTOUCHED
        ) {
            binding.tilTouchedUntouched.setErrorMessages(MSG_SELECT_TOUCHED_UNTOUCHED)
            false
        } else if (binding.tilFeedback.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spFeedback
            ) == SELECT_FEEDBACK
        ) {
            binding.tilFeedback.setErrorMessages(MSG_SELECT_FEEDBACK)
            false
        }else if (enableLocationGroup && !permissionCheckHelper.locationPermission()) {
            permissionCheckHelper.requestLocation()
            false
        } else if (enableLocationGroup && !(requireContext().getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
            && CRMMOSLCommon.hasGPSDevice(requireContext())
        ) {
            //fusedLocation.initDispatcher(this)
            val dialog = GeoTaggingFragment()
            dialog.setData(uiSubject)
            dialog.show(activity?.supportFragmentManager!!, "UserAddress")

            false
        } else true
        uiSubject.onNext(ValidationStatus(status))
        return status
    }

    override fun validateMobileNumber(): Boolean {
        binding.tilMobileNumber.disableErrorMessages()
        return if (getMobileNumberType() == CreateMeetingConstants.SELECT_MOBILE_NUMBER) {
            binding.tilMobileNumber.setErrorMessages(MSG_SELECT_MOBILE_NUMBER)
            false
        } else true
    }

    override fun onAttendeeClicked(): Observable<Unit> {
        return binding.ibAttendee.clicks()
    }

    override fun onAttendeeAddedClicked(): Observable<Unit> {
        return binding.tvAttendeeCount.clicks()
    }

    override fun onSplAttendeeClicked(): Observable<Unit> {
        return binding.ibSplAttendee.clicks()
    }

    override fun onSplAttendeeAddedClicked(): Observable<Unit> {
        return binding.tvSplAttendeeCount.clicks()
    }

    override fun onCategorySelected(): Observable<Int> {
        return binding.spCategory.itemSelections()
    }

    override fun onMeetingLocationSelected(): Observable<Int> {
        return binding.spMeetingLocation.itemSelections()
    }

    override fun onMeetingReasonSelected(): Observable<Int> {
        return binding.spMeetingReason.itemSelections()
    }

    override fun onMobileNumberSelected(): Observable<Int> {
        return binding.spMobileNumber.itemSelections()
    }

    override fun onActivityTypeSelected(): Observable<Int> {
        return binding.spActivityType.itemSelections()
    }

    override fun onProductSelected(): Observable<Int> {
        return binding.spProduct.itemSelections()
    }

    override fun getSelectedCategory(): LookUp? {
        return getSelectedSpinnerValue(binding.spCategory)
    }

    override fun getSelectedTouchedUntouched(): LookUp? {
        return getSelectedSpinnerValue(binding.spTouchedUntouched)
    }

    override fun getSelectedFeedback(): LookUp? {
        return getSelectedSpinnerValue(binding.spFeedback)
    }

    private fun getSelectedSpinnerValue(spinner: Spinner): LookUp? {
        if (spinner.selectedItem is LookUp) {
            return (spinner.selectedItem as LookUp)
        }
        return null
    }

    override fun getSelectedMeetingLocation(): LookUp {
        return (binding.spMeetingLocation.selectedItem as LookUp)
    }

    override fun getSelectedTravellingMode(): LookUp {
        return (binding.spTravellingMode.selectedItem as LookUp)
    }

    override fun getSelectedMeetingDuration(): LookUp? {
        return getSelectedSpinnerValue(binding.spMeetingDuration)
    }

    override fun getSelectedMeetingObjective(): LookUp? {
        return getSelectedSpinnerValue(binding.spMeetingObjective)
    }

    override fun getSelectedSubCategory(): LookUp? {
        return getSelectedSpinnerValue(binding.spSubCategory)
    }

    override fun getSelectedMeetingReason(): LookUp {
        return binding.spMeetingReason.selectedItem as LookUp
    }

    override fun getSelectedActivityType(): LookUp {
        return (binding.spActivityType.selectedItem as LookUp)
    }

    override fun getSelectedEvent(): String? {
        if (binding.spChips.selectedItem is String ) {
            return (binding.spChips.selectedItem as String)
        }
        return null
    }

    override fun getMobileNumberType(): MobileListData {
        return (binding.spMobileNumber.selectedItem as MobileListData)
    }

    override fun getSelectedProduct(): LookUp {
        return (binding.spProduct.selectedItem as LookUp)
    }

    override fun getSelectedActionable(): LookUp? {
        return getSelectedSpinnerValue(binding.spActionables)
    }

    override fun getPincode(): String {
        return binding.etPinCode.text.toString()
    }

    override fun getAmount(): String {
        return binding.etAmount.text.toString()
    }

    override fun getMeetingAddress(): String {
        return binding.etLocation.text.toString()
    }

    override fun getMeetingDesc(): String {
        return binding.etMeetingDescription.text.toString()
    }

    override fun getCustomerComment(): String? {
        return binding.etCustomerComment.text.toString()
    }

    override fun getCategory(): String {
        return binding.etCategory.text.toString()
    }

    override fun getSubCategory(): String {
        return binding.etSubCategory.text.toString()
    }

    override fun toggleOTPVisibility(show: Boolean) {
        enableDisableField(binding.llLocationGroup, show)
        if (strActivateOTPInApp.equals("Yes", ignoreCase = true)) {
            if (show) {
                binding.btnSendOtp.visibility = View.VISIBLE
                binding.tilMobileNumber.visibility = View.VISIBLE
            } else {
                binding.btnSendOtp.visibility = View.GONE
                binding.tilMobileNumber.visibility = View.GONE
            }
        }else{
            binding.btnSendOtp.visibility = View.GONE
            binding.tilMobileNumber.visibility = View.GONE
        }

        if (userManager.getUser()?.EventListRequired == true && strSourceName == "Manual" && isBTLProspectSource()){
                uiSubject.onNext(CallChipsApi)
                binding.tilChips.isVisible = true
        }else{
            binding.tilChips.isVisible = false
        }
        resetDataOnActivityTypeChange()
    }

    private fun isBTLProspectSource(): Boolean{
        if(strProspectSource.equals("BTL activity", ignoreCase = true) ||
            strProspectSource.equals("BTL activity QR code", ignoreCase = true)){
            return true
        }
        return false
    }

    override fun onMobileClick() {
        binding.btnSendOtp.text = SEND_OTP_LABEL
    }

    override fun toggleActionableVisibility(show: Boolean) {
        if (show) {
            binding.llActionables.visibility = View.VISIBLE
        } else {
            binding.llActionables.visibility = View.GONE
            binding.llSubcategory.gone()
        }
    }

    override fun checkPermissionAndProceed() {
        if (checkPermission()) {
            // Toast.makeText(requireContext(),"check permission succed",Toast.LENGTH_LONG).show()
            if (strAddress.isNotEmpty() && strLatitude.isNotEmpty() && strLongitude.isNotEmpty()) {
                //  Toast.makeText(requireContext(),"location != null, call SendOtpRequest",Toast.LENGTH_LONG).show()

                uiSubject.onNext(SendOtpRequest)
            } else {
                //fusedLocation.LocationUpdates()
                isLocEn = false

                if (permissionCheckHelper.locationPermission()) {
                    //  Toast.makeText(requireContext(),"going to else in location != null",Toast.LENGTH_LONG).show()

                    //fusedLocation.initDispatcher(this)
                    val dialog = GeoTaggingFragment()
                    dialog.setData(uiSubject)
                    dialog.show(activity?.supportFragmentManager!!, "UserAddress")
                }
            }
        }
    }

    override fun sendOTP(): Observable<SendOtpRequest> {
        return uiSubject.ofType()
    }

    override fun onDataValidated(): Observable<ValidationStatus> {
        return uiSubject.ofType()
    }

    override fun showOTPDialog(
        ucid: String,
        leadId: String,
        mobileNumber: String
    ) {
        val ft =
            requireActivity().supportFragmentManager
                .beginTransaction()

        val dialogFragment = OTPFragment.newInstance(leadId, ucid, mobileNumber)
        dialogFragment.setTargetFragment(this, 1)
        dialogFragment.setListener(this)
        dialogFragment.show(ft, "")
    }

    private fun checkPermission(): Boolean {
        if (!permissionCheckHelper.locationPermission()) {
            // Toast.makeText(requireContext(),"permissionCheckHelper.locationPermission() = false",Toast.LENGTH_LONG).show()

            /*if(VERSION.SDK_INT > VERSION_CODES.Q) {
                if(checkMapServices()) {
                    if(!mLocationPermissionGranted) {
                        getLocationPermission()
                    }
                }
            }*/
            //permissionCheckHelper.requestLocation()

            if(checkMapServices()) {
                if(!mLocationPermissionGranted) {
                    //  Toast.makeText(requireContext(),"mLocationPermissionGranted = false",Toast.LENGTH_LONG).show()

                    getLocationPermission()
                }
            }
            return false
        } else if (!(requireContext().getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
            && CRMMOSLCommon.hasGPSDevice(requireContext())
        ) {
            // Toast.makeText(requireContext(),"call fusedLocation.initDispatcher",Toast.LENGTH_LONG).show()
            val dialog = GeoTaggingFragment()
            dialog.setData(uiSubject)
            dialog.show(activity?.supportFragmentManager!!, "UserAddress")
            //fusedLocation.initDispatcher(this)

            return false
        }
        return true
    }

    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            //  Toast.makeText(requireContext(),"isServicesOK() = true",Toast.LENGTH_LONG).show()

            if (isMapsEnabled()) {
                //   Toast.makeText(requireContext(),"isMapsEnabled() = true",Toast.LENGTH_LONG).show()
                return true
            }
        }
        // Toast.makeText(requireContext(),"isMapsEnabled() = false",Toast.LENGTH_LONG).show()

        return false
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            }
        val alert = builder.create()
        alert.show()
    }

    fun isMapsEnabled(): Boolean {
        val manager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun getLocationPermission() {
        /*
             * Request location permission, so that we can get the location of the
             * device. The result of the permission request is handled by a callback,
             * onRequestPermissionsResult.
             */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            //  Toast.makeText(requireContext(),"request for PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION",Toast.LENGTH_LONG).show()

        }
    }

    fun isServicesOK(): Boolean {
        Log.d("TAG", "isServicesOK: checking google services version")
        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("TAG", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("TAG", "isServicesOK: an error occured but we can fix it")
            val dialog: Dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(requireActivity(), available, ERROR_DIALOG_REQUEST)!!
            dialog.show()
        } else {
            Toast.makeText(requireContext(), "You can't make map requests", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    if (permissionCheckHelper.locationPermission()) {
                        // fusedLocation.initDispatcher(this)
                        val dialog = GeoTaggingFragment()
                        dialog.setData(uiSubject)
                        dialog.show(activity?.supportFragmentManager!!, "UserAddress")

                    }
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult: called.")
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {
                    /*if (checkPermission()) {
                        if (location != null) {
                            uiSubject.onNext(SendOtpRequest)
                        } else {
                            fusedLocation.LocationUpdates()
                        }
                    }*/
                } else {
                    getLocationPermission()
                }
            }
        }
    }

    private var isSendOTPSuccesssful = false
    private var isPotentialPMSProduct = false
    private var isOnlyClient = false
    private var enableLocationGroup = false
    override fun onSuccessResult(status: Boolean) {
        isSendOTPSuccesssful = status
    }

    override fun cancelClick() {
        binding.btnSendOtp.text = SEND_OTP_LABEL
    }

    override fun isOTPSuccessful(): Boolean {
        return isSendOTPSuccesssful
    }


    override fun showToast() {
        Toast.makeText(requireContext(), "OTP is already verified.", Toast.LENGTH_LONG)
            .show()
    }

    override fun renderOTP(uiModel: UiModel<SendOtpResponse>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderOTP showOnFailurePopup ${it.message}")

            showOnFailurePopup(it) }

        uiModel.onSuccess {
            if(it.status ){
                Toast.makeText(requireContext(), "OTP sent successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.btnSendOtp.setText(string.btn_label_verify_otp)
            }else{
                Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onTravellingModeSelected(): Observable<Int> {
        return binding.spTravellingMode.itemSelections()
    }

    override fun setInputData(meetingApptModel: MeetingApptModel) {
        Timber.i("Meeting JSon %s", meetingApptModel.json)
        if (enableLocationGroup) {
            if (checkPermission()) {
                if (strLatitude.isNotEmpty()&& strLongitude.isNotEmpty()&& strAddress.isNotEmpty()) {
                    isMockLocation(meetingApptModel)
                } else {
                    val dialog = GeoTaggingFragment()
                    dialog.setData(uiSubject)
                    dialog.show(activity?.supportFragmentManager!!, "UserAddress")
                }
            }
        } else {
            isMockLocation(meetingApptModel)
        }

    }

    private fun isMockLocation(meetingApptModel: MeetingApptModel) {
        if (enableLocationGroup && isMockLocationEnabled ) {
            MSFDialog.alertDialog(
                requireContext(), 0,
                resources.getString(string.motilal_oswal),
                MOSLConstants.MOCK_LOCATION_ENABLED_MSG, "OK",
                true
            ) { action: Action, _: Array<Any?>? ->
                if (action == OK) {
                    requireActivity().finish()
                }
            }
        } else {
            if (isPotentialPMSProduct && !isOnlyClient) {
                MSFDialog.alertDialog(
                    requireContext(),
                    0,
                    resources.getString(string.motilal_oswal),
                    MOSLConstants.POTENTIAL_INVEST_IN_PRODUCT,
                    MOSLConstants.YES,
                    MOSLConstants.NO,
                    true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        meetingApptModel.isPotentialPMSProduct = "YES"
                        uiSubject.onNext(SaveRequestData(meetingApptModel.json))
                    } else {
                        meetingApptModel.isPotentialPMSProduct = "NO"
                        uiSubject.onNext(SaveRequestData(meetingApptModel.json))
                    }
                }
            } else {
                meetingApptModel.isPotentialPMSProduct = ""
                uiSubject.onNext(SaveRequestData(meetingApptModel.json))
            }
        }
    }

    override fun renderSaveRequest(uiModel: UiModel<AddUpdateMeeting>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("CreateMeetingFragment renderSaveRequest showOnFailurePopup ${it.message}")

            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            MSFDialog.alertDialog(
                requireContext(), 0, resources.getString(string.motilal_oswal),
                MOSLConstants.CREATE_MEETING, "OK", true
            )
            { action: Action, _: Array<Any?>? ->
                if (action == OK) {
                    uiSubject.onNext(SaveConfirmClick)
                }
            }
        }
    }

    companion object {
        const val ARG_LEAD_ID = "LeadID"
        const val ARG_UCID = "UCID"
        const val SEND_OTP_LABEL = "Send OTP"
        const val VERIFY_OTP_LABEL = "Verify OTP"
    }
}