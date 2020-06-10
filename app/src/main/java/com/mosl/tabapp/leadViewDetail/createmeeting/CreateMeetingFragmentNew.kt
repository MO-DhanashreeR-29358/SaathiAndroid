package com.mosl.tabapp.leadViewDetail.createmeeting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.*
import com.mosl.mobile.pdf.OTPFragment
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE11
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE12
import com.mosl.shared.util.getFormatedDate
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.R.string
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMMOSLCommon
import com.mosl.tabapp.common.FusedLocation
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
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_DURATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_LOCATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_OBJECTIVE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_MEETING_REASON
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_PRODUCT
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_SUB_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.MSG_SELECT_TRAVELLING_MODE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_ACTIONABLE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_DURATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_LOCATION
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_OBJECTIVE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_MEETING_REASON
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_PRODUCT
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_SUB_CATEGORY
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.SELECT_TRAVELLING_MODE
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.hasAttendeeAdded
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.*
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.setErrorMessages
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreateMeetingFragmentNew : CommonFragment(), CreateMeetingView, OTPFragment.OTPDialogListener,
    FusedLocation.LatLongDispatcher, AddedAttendeeAdapter.ClickListenerHandler {
    lateinit var binding: FragCreateMeetingBinding
    private lateinit var attendeeAdapter: AttendeeAdapter
    private lateinit var addedAttendeeAdapter: AddedAttendeeAdapter
    private lateinit var splAttendeeAdapter: AttendeeAdapter
    private lateinit var addedSplAttendeeAdapter: AddedAttendeeAdapter

    @Inject
    lateinit var presenter: CreateMeetingPresenter

    @Inject
    lateinit var permissionCheckHelper: PermissionCheckHelperFragment

    @Inject
    lateinit var fusedLocation: FusedLocation
    private lateinit var productAdapter: ArrayAdapter<LookUp>
    private lateinit var categoryAdapter: ArrayAdapter<LookUp>
    private lateinit var subCategoryAdapter: ArrayAdapter<LookUp>
    private lateinit var actionableAdapter: ArrayAdapter<LookUp>
    private lateinit var activityAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingObjectiveAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingDurationAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingLocationAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingReasonAdapter: ArrayAdapter<LookUp>
    private lateinit var travellingModeAdapter: ArrayAdapter<LookUp>
    private lateinit var outstationAdapter: ArrayAdapter<LookUp>
    private lateinit var baClientAdapter: ArrayAdapter<LookUp>
    private lateinit var meetingProductAdapter: CreateMeetingProductAdapter
    private lateinit var travellingAdapter: CreateMeetingTravellingModeAdapter
    private val uiSubject = PublishSubject.create<CreateMeetingUiEvent>()
    private lateinit var mainFromDate: String
    override fun getLayout() = layout.frag_create_meeting

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragCreateMeetingBinding.bind(view)
        setAppTitle(string.page_title_create_meeting)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        setupViews()
        if (permissionCheckHelper.locationPermission()) {
            fusedLocation.initDispatcher(this)
        }
    }

    private fun setupViews() {
        productAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spProduct.adapter = productAdapter

        activityAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActivityType.adapter = activityAdapter

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

        categoryAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spCategory.adapter = categoryAdapter

        actionableAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spActionables.adapter = actionableAdapter

        subCategoryAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spSubCategory.adapter = subCategoryAdapter

        meetingProductAdapter = CreateMeetingProductAdapter(requireContext())
        binding.rvProductList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvProductList.adapter = meetingProductAdapter
        travellingAdapter = CreateMeetingTravellingModeAdapter(requireContext())
        binding.rvTravellingList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvTravellingList.adapter = travellingAdapter

    }

    override fun onDataSaved(): Observable<SaveRequestData> {
        return uiSubject.ofType()
    }

    override fun onSaveConfirmClick(): Observable<SaveConfirmClick> {
        return uiSubject.ofType()
    }

    override fun render(uiModel: UiModel<ClientData>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            val clientDetail = it.clientDetail
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
            meetingDurationAdapter.setSpinnerData(binding.spMeetingDuration, it.listMeetingDuration)
            travellingModeAdapter.setSpinnerData(binding.spTravellingMode, it.listTravellingMode)
            isOnlyClient = clientDetail.isOnlyClient
            isPotentialPMSProduct = clientDetail.isPotentialPMSProduct.isEmpty()
            mainFromDate = CRMAppUtil.getDate(Date(), yyyy_MM_dd)
        }
    }

    override fun onDatePicker(): Observable<Unit> {
        return binding.rlDate.clicks()
    }

    override fun renderDatePicker() {
        showSelectDateDialog()
    }

    private fun showSelectDateDialog() {
        var hour: Int = 0
        var minute: Int = 0

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.date_picker)
            window
                .setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        val dpFromDate = dialog.findViewById<DatePicker>(R.id.datePickerFromDate)
        dpFromDate.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        val close =
            dialog.findViewById<ImageView>(R.id.ibtnClose)
        val btnSubmit =
            dialog.findViewById<Button>(R.id.btnSubmit)
        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
        timePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        if (getSelectedActivityType()!!.lookUpName.equals("Personal Meeting", ignoreCase = true)
            && userManager.getUser()!!.isRM()
        ) {
            dpFromDate.minDate = System.currentTimeMillis() - 1000
        } else {
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

        timePicker.setOnTimeChangedListener { arg0: TimePicker?, arg1: Int, arg2: Int ->
            hour = arg1
            minute = arg2
        }

        close.setOnClickListener { dialog.dismiss() }

        btnSubmit.setOnClickListener {
            try {
                val fromDat = CRMAppUtil.getDate(mainFromDate, yyyy_MM_dd)
                val fromPostingDate = CRMAppUtil.getDateStr(fromDat, dd_MM_yyyy)
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
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.etLocation.setText(it)
        }
    }

    override fun renderMeetingReason(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            meetingReasonAdapter.clear()
            meetingReasonAdapter.setSpinnerData(binding.spMeetingReason, it)
        }
    }

    override fun renderSubCategory(uiModel: UiModel<List<LookUp>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure { showOnFailurePopup(it) }

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
        return getFormatedDate(binding.tvDate.text.toString(), DF_JSON_DATE12, DF_JSON_DATE11)

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
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_attendee_list)
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_attendee_list)
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(layout.dialog_addedd_attendee_list)
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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

    var location: Location? = null

    override fun onSendOTPClicked(): Observable<Unit> {
        return binding.btnSendOtp.clicks()
    }

    override fun getOTPButtonText(): String {
        return binding.btnSendOtp.text.toString()
    }

    override fun getLatitude(): String {
        return location?.latitude.toString()
    }

    override fun getLongitude(): String {
        return location?.longitude.toString()
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
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        location?.apply {
            val addressLine = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressLine.size > 0) {
                geoArea = addressLine[0].getAddressLine(0)
            }
        }

        return geoArea
    }

    override fun onSaveClicked(): Observable<Unit> {
        return binding.btnSave.clicks()
    }

    override fun onResetClicked(): Observable<Unit> {
        return binding.btnReset.clicks()
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
        binding.spMeetingReason.setSelection(0)
        binding.spMeetingObjective.setSelection(0)
        binding.spMeetingLocation.setSelection(0)
        binding.spTravellingMode.setSelection(0)
        binding.etLocation.setText("")
        binding.etPinCode.setText("")
        binding.etAmount.setText("")
        binding.etMeetingDescription.setText("")
        binding.etCustomerComment.setText("")
        binding.btnSendOtp.text = SEND_OTP_LABEL
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
        binding.tilPinCode.disableErrorMessages()
        binding.tilEtCategory.disableErrorMessages()
        binding.tilEtSubCategory.disableErrorMessages()
    }

    override fun validateFormData(): Boolean {
        errorDisabled()
        val status: Boolean = if (userManager.getUser()!!.isRM() && !isSendOTPSuccesssful &&
            getSelectedActivityType()!!.isPersonalMeeting()
        ) {
            alert("Please verify OTP to create meeting")
            false
        } else if (getSelectedActivityType()!! == CreateMeetingConstants.SELECT_ACTIVITY_TYPE) {
            binding.tilActivityType.setErrorMessages(MSG_SELECT_ACTIVITY_TYPE)
            false
        } else if (getSelectedSpinnerValue(binding.spProduct)!! == SELECT_PRODUCT && meetingProductAdapter.listProduct.size == 0) {
            binding.tilProduct.setErrorMessages(MSG_SELECT_PRODUCT)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingDuration)!! == SELECT_MEETING_DURATION
        ) {
            binding.tilMeetingDuration.setErrorMessages(MSG_SELECT_MEETING_DURATION)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingObjective)!! == SELECT_MEETING_OBJECTIVE
        ) {
            binding.tilMeetingObjective.setErrorMessages(MSG_SELECT_MEETING_OBJECTIVE)
            false
        } else if (getSelectedSpinnerValue(binding.spMeetingReason)!! == SELECT_MEETING_REASON) {
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
        } else if (enableLocationGroup && getSelectedSpinnerValue(binding.spMeetingLocation)!! == SELECT_MEETING_LOCATION
        ) {
            binding.tilMeetingLocation.setErrorMessages(MSG_SELECT_MEETING_LOCATION)
            false
        } else if (enableLocationGroup && getSelectedSpinnerValue(binding.spTravellingMode)!! == SELECT_TRAVELLING_MODE && travellingAdapter.travellingList.size == 0
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
            )!! == SELECT_ACTIONABLE
        ) {
            binding.tilActionables.setErrorMessages(MSG_SELECT_ACTIONABLE)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spCategory
            )!! == SELECT_CATEGORY
        ) {
            binding.tilCategory.setErrorMessages(MSG_SELECT_CATEGORY)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && binding.etCategory.text!!.length <= 10) {
            binding.tilEtCategory.setErrorMessages(MSG_ATLEAST_TEN_CHARACTERS)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && getSelectedSpinnerValue(
                binding.spSubCategory
            )!! == SELECT_SUB_CATEGORY
        ) {
            binding.tilSubCategory.setErrorMessages(MSG_SELECT_SUB_CATEGORY)
            false
        } else if (binding.llActionables.visibility == View.VISIBLE && binding.etSubCategory.text!!.length <= 10) {
            binding.tilEtSubCategory.setErrorMessages(MSG_ATLEAST_TEN_CHARACTERS)
            false
        } else if (enableLocationGroup && !permissionCheckHelper.locationPermission()) {
            permissionCheckHelper.requestLocation()
            false
        } else if (enableLocationGroup && !(requireContext().getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
            && CRMMOSLCommon.hasGPSDevice(requireContext())
        ) {
            fusedLocation.initDispatcher(this)
            false
        } else true
        uiSubject.onNext(ValidationStatus(status))
        return status
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

    override fun onActivityTypeSelected(): Observable<Int> {
        return binding.spActivityType.itemSelections()
    }

    override fun onProductSelected(): Observable<Int> {
        return binding.spProduct.itemSelections()
    }

    override fun getSelectedCategory(): LookUp? {
        return getSelectedSpinnerValue(binding.spCategory)
    }

    private fun getSelectedSpinnerValue(spinner: Spinner): LookUp? {
        if (spinner.selectedItem is LookUp) {
            return spinner.selectedItem as LookUp
        }
        return null
    }

    override fun getSelectedMeetingLocation(): LookUp? {
        if (binding.spMeetingLocation.selectedItem is LookUp) {
            return (binding.spMeetingLocation.selectedItem as LookUp)
        }
        return null
    }

    override fun getSelectedTravellingMode(): LookUp? {
        if (binding.spTravellingMode.selectedItem is LookUp) {
            return (binding.spTravellingMode.selectedItem as LookUp)
        }
        return null
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

    override fun getSelectedMeetingReason(): LookUp? {
        if (binding.spMeetingReason.selectedItem is LookUp) {
            return binding.spMeetingReason.selectedItem as LookUp
        }
        return null
    }

    override fun getSelectedActivityType(): LookUp? {
        if (binding.spActivityType.selectedItem is LookUp) {
            return (binding.spActivityType.selectedItem as LookUp)
        }
        return null
    }

    override fun getSelectedProduct(): LookUp? {
        if (binding.spProduct.selectedItem is LookUp) {
            return (binding.spProduct.selectedItem as LookUp)
        }
        return null
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
        if (userManager.getUser()!!.isRM()) {
            if (show) {
                binding.btnSendOtp.visibility = View.VISIBLE
                enableLocationGroup = true
                enableField(binding.llLocationGroup)
            } else {
                binding.btnSendOtp.visibility = View.GONE
                enableLocationGroup = false
                disableField(binding.llLocationGroup)
            }
        }
    }

    override fun toggleActionableVisibility(show: Boolean) {
        if (show) {
            binding.llActionables.visibility = View.VISIBLE
        } else {
            binding.llActionables.visibility = View.GONE
        }
    }

    override fun checkPermissionAndProceed() {
        if (checkPermission()) {
            if (location != null) {
                uiSubject.onNext(SendOtpRequest)
            } else {
                fusedLocation.LocationUpdates()
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
        leadId: String
    ) {
        val ft =
            requireActivity().supportFragmentManager
                .beginTransaction()

        val dialogFragment = OTPFragment.newInstance(leadId, ucid)
        dialogFragment.setTargetFragment(this, 1)
        dialogFragment.setListener(this)
        dialogFragment.show(ft, "")
    }

    private fun checkPermission(): Boolean {
        if (!permissionCheckHelper.locationPermission()) {
            permissionCheckHelper.requestLocation()
            return false
        } else if (!(requireContext().getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
            && CRMMOSLCommon.hasGPSDevice(requireContext())
        ) {
            fusedLocation.initDispatcher(this)
            return false
        }
        return true
    }

    private var isSendOTPSuccesssful = false
    private var isPotentialPMSProduct = false
    private var isOnlyClient = false
    private var enableLocationGroup = false
    override fun onSuccessResult(status: Boolean) {
        isSendOTPSuccesssful = status
    }

    override fun isOTPSuccessful(): Boolean {
        return isSendOTPSuccesssful
    }

    override fun onLatLongChanged(location: Location?) {
        this.location = location!!
    }

    override fun showToast() {
        Toast.makeText(requireContext(), "OTP is already verified.", Toast.LENGTH_LONG)
            .show()
    }

    override fun renderOTP(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure { showOnFailurePopup(it) }

        uiModel.onSuccess {
            Toast.makeText(requireContext(), "OTP sent successfully", Toast.LENGTH_SHORT)
                .show()
            binding.btnSendOtp.setText(R.string.btn_label_verify_otp)
        }
    }

    override fun onTravellingModeSelected(): Observable<Int> {
        return binding.spTravellingMode.itemSelections()
    }

    override fun setInputData(meetingApptModel: MeetingApptModel) {
        if (enableLocationGroup) {
            if (checkPermission()) {
                if (location != null) {
                    isMockLocation(meetingApptModel)
                } else {
                    fusedLocation.LocationUpdates()
                }
            }
        } else {
            isMockLocation(meetingApptModel)
        }

    }

    private fun isMockLocation(meetingApptModel: MeetingApptModel) {
        if (enableLocationGroup && CRMAppUtil.isLocationFromMockProvider(
                requireContext(),
                location
            )
        ) {
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