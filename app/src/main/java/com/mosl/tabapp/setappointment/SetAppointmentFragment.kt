package com.mosl.tabapp.setappointment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.TimePicker
import android.widget.Toast
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.common.RowData.getSpinnerRowDataById
import com.mosl.tabapp.constants.MOSLConstants.ACTIVITYTYPE
import com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE
import com.mosl.tabapp.constants.MOSLConstants.ATTENDEE_ALREADY_ADDED
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.constants.MOSLConstants.DAYS
import com.mosl.tabapp.constants.MOSLConstants.END_TIME_EQUAL_ERROR
import com.mosl.tabapp.constants.MOSLConstants.END_TIME_ERROR
import com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN
import com.mosl.tabapp.constants.MOSLConstants.LEAD_ID
import com.mosl.tabapp.constants.MOSLConstants.MEETING_ID
import com.mosl.tabapp.constants.MOSLConstants.REPEATMODE
import com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIVITY_TYPE
import com.mosl.tabapp.constants.MOSLConstants.SELECT_APPT_DATE_TYPE
import com.mosl.tabapp.constants.MOSLConstants.SELECT_PRODUCT
import com.mosl.tabapp.constants.MOSLConstants.SELECT_WEEK
import com.mosl.tabapp.constants.MOSLConstants.SELECT_WEEK_DAYS
import com.mosl.tabapp.constants.MOSLConstants.SET_APPOINTMENTS
import com.mosl.tabapp.constants.MOSLConstants.SPL_ATTENDEE_ALREADY_ADDED
import com.mosl.tabapp.constants.MOSLConstants.START_TIME_ERROR
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.constants.MOSLConstants.UPDATE_APPOINTMENTS
import com.mosl.tabapp.constants.MOSLConstants.WEEKS
import com.mosl.tabapp.constants.MOSLConstants.mFormatWithDash
import com.mosl.tabapp.constants.MOSLConstants.mFormatWithSpace
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragSetAppiontmentBinding
import com.mosl.tabapp.databinding.ItemListAddedAttendeeBinding
import com.mosl.tabapp.databinding.ItemListAddedSplAttendeeBinding
import com.mosl.tabapp.leadViewDetail.SetAppointmentFragment
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.model.ClientDetail
import com.mosl.tabapp.model.EmployeeModel
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.model.SplAttendeeModel
import com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import kotlinx.android.synthetic.main.frag_set_appiontment.spRepeateOn
import kotlinx.android.synthetic.main.frag_set_appiontment.spRepeateWeek
import kotlinx.android.synthetic.main.frag_set_appiontment.spSelectProduct
import kotlinx.android.synthetic.main.frag_set_appiontment.spSelectWeekDay
import kotlinx.android.synthetic.main.frag_set_appiontment.tvAPPDateError
import kotlinx.android.synthetic.main.frag_set_appiontment.tvApppointmentDateTime
import kotlinx.android.synthetic.main.frag_set_appiontment.tvApppointmentDateTimeDisplay
import kotlinx.android.synthetic.main.frag_set_appiontment.tvEndDateError
import kotlinx.android.synthetic.main.frag_set_appiontment.tvEndDateTime
import kotlinx.android.synthetic.main.frag_set_appiontment.tvEndDateTimeDisplay
import kotlinx.android.synthetic.main.frag_set_appiontment.tvRepeatTypeSpError
import kotlinx.android.synthetic.main.frag_set_appiontment.tvRepeatWeekdayError
import kotlinx.android.synthetic.main.frag_set_appiontment.tvStartDateError
import kotlinx.android.synthetic.main.frag_set_appiontment.tvStartDateTime
import kotlinx.android.synthetic.main.frag_set_appiontment.tvStartDateTimeDisplay
import kotlinx.android.synthetic.main.lms_top_section_update.ivLead
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber
import java.net.URLEncoder
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class SetAppointmentFragment : CommonFragment(), OnClickListener, OnEditorActionListener {

  private lateinit var binding: FragSetAppiontmentBinding

  private val arrSelectProduct = ArrayList<RowData>()
  private val arrSelectWeek = ArrayList<RowData>()
  private val arrSelectWeekDay = ArrayList<RowData>()
  private val arrActivityType = ArrayList<RowData>()
  private val arrRepeatMode = ArrayList<RowData>()

  private var addedAttendeeCount: Int = 0
  private var addedSplAttendeeCount: Int = 0
  private var str_LeadID = ""
  private var productCodeStr: String? = null
  private val addedAttendeeModelList = ArrayList<AddedAttendeeModel>()
  private var attendeeAdapter: AttendeeAdapter? = null
  private val addedSplAttendeeModelList = ArrayList<AddedSplAttendeeModel>()
  private var splAttendeeAdapter: SplAttendeeAdapter? = null
  private var weekCodeStr: String? = null
  private var weekDayCodeStr: String? = null
  private var today: Calendar? = null
  private var dialogDateTime: Dialog? = null
  private var hour: Int = 0
  private var minute: Int = 0
  private var mainFromDate: String? = null
  private var fromPostingDate: String? = null
  private var mainFromDateStartDate: String? = null
  private var fromPostingDateStartDate: String? = null
  private var hourStartDate: Int = 0
  private var minuteStartDate: Int = 0
  private var mainFromDateEndDate: String? = null
  private var fromPostingDateEndDate: String? = null
  private var hourEndDate: Int = 0
  private var minuteEndDate: Int = 0
  private var currentYear: Int = 0
  private var month: Int = 0
  private var day: Int = 0
  private var currentYearStartDate: Int = 0
  private var monthStartDate: Int = 0
  private var dayStartDate: Int = 0
  private var currentYearEndDate: Int = 0
  private var monthEndDate: Int = 0
  private var dayEndDate: Int = 0
  private var activityTypeCodeStr: String? = null
  private var fromDat: Date? = null//, toDate;
  private var meetingId: String? = ""
  private var createMode = true
  private var mUCID = "0"

  private var close: ImageView? = null
  private var btnSubmit: Button? = null

  override fun getLayout(): Int {
    return R.layout.frag_set_appiontment
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragSetAppiontmentBinding.bind(view)
    setAppTitle("Set Appointment")
    setUpFromArguments()
    setUpView()
    if (!mUCID.equals("0", ignoreCase = true)) {
      getClientDetail(mUCID)
      setProfileImage(mUCID)
    } else {
      getLeadByIdFromServer(str_LeadID)
    }

  }

  private fun getLeadByIdFromServer(strLeadId: CharSequence) {
    CRMAppUtil.setImage(ByteArray(0), ivLead, requireContext())
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LeadViewRequest(strLeadId.toString())
      val subscribe = saathiApi.getLeadView(request)
          .compose(applyUiModel())
          .subscribe { this.renderLeadView(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderLeadView(uiModel: UiModel<LeadViewItem>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      val leadId = stringOrEmpty(data.leadID.toString())
      val branchCode = stringOrEmpty(data.branchcode)
      val contact = data.contact
      val name = stringOrEmpty(contact!!.name)

      val leadProductMapping = data.leadProductMapping
      for (i in leadProductMapping!!.indices) {
        val (_, _, _, _, _, _, _, _, pendingDays1, _, _, _, _, _, _, _, _, isDefault1, _, stageName1, _, _, _, _, _, _, _, _, _, _, _, _, clientCode1) = leadProductMapping[i]
        val clientCode = stringOrEmpty(clientCode1)
        val stageName = stringOrEmpty(stageName1)
        val isDefault = stringOrEmpty(isDefault1.toString())
        val pendingDays = stringOrEmpty(pendingDays1.toString())
        if (isDefault.equals("true", ignoreCase = true)) {
          binding.includeLmsTopSectionUpdate.tvClientCode.text = clientCode
          binding.includeLmsTopSectionUpdate.tvCurrentStage.text = stageName
          binding.includeLmsTopSectionUpdate.tvCurrentStageSince.text = "$pendingDays Days"
        }
        binding.includeLmsTopSectionUpdate.tvName.text = name
        binding.includeLmsTopSectionUpdate.tvLeadId.text = leadId
        binding.includeLmsTopSectionUpdate.tvBranchCode.text = branchCode
      }
    }
  }

  private fun setProfileImage(ucid: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = ClientImageRequest(ucid)
      val subscribe = saathiApi.getClientImage(request)
          .compose(applyUiModel())
          .subscribe { this.renderClientImage(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderClientImage(uiModel: UiModel<SaathiCommonResponse<String>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      val response = it.response
      val byt = Base64.decode(response, Base64.DEFAULT)
      if (byt != null) {
        CRMAppUtil.setImage(byt, ivLead, requireContext())
      } else {
        CRMAppUtil.setImage(
            ByteArray(0), ivLead,
            requireContext()
        )
      }
    }
  }

  private fun getClientDetail(ucid: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = ClientDetailRequest(Integer.valueOf(ucid))
      val subscribe = saathiApi.getClientDetail(request)
          .compose(applyUiModel())
          .subscribe { this.renderClientDetails(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderClientDetails(uiModel: UiModel<ClientDetailItem>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      ClientDetail(it)
      binding.includeLmsTopSectionUpdate.tvLeadId.text = ClientDetail.getLeadId()
      binding.includeLmsTopSectionUpdate.tvCurrentStage.text = ClientDetail.getCurrentStage()
      if (!ClientDetail.getCurrentStageSince().isEmpty()) {
        binding.includeLmsTopSectionUpdate.tvCurrentStageSince.text =
          "${ClientDetail.getCurrentStageSince()} Days"
      }
      binding.includeLmsTopSectionUpdate.tvName.text = ClientDetail.getName()
      binding.includeLmsTopSectionUpdate.tvClientCode.text = ClientDetail.getClientCode()
      binding.includeLmsTopSectionUpdate.tvBranchCode.text = ClientDetail.getBranchCode()
    }
  }

  private fun setUpView() {
    binding.scrollView.apply {
      descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
      isFocusable = true
      isFocusableInTouchMode = true
      setOnTouchListener { v, _ ->
        v.requestFocusFromTouch()
        false
      }
    }

    binding.apply {
      ibAttendee.setOnClickListener(this@SetAppointmentFragment)
      ibSplAttendee.setOnClickListener(this@SetAppointmentFragment)
      tvAttendeeCount.setOnClickListener(this@SetAppointmentFragment)
      tvSplAttendeeCount.setOnClickListener(this@SetAppointmentFragment)
      tvApppointmentDateTime.setOnClickListener(this@SetAppointmentFragment)
      tvApppointmentDateTimeDisplay.setOnClickListener(this@SetAppointmentFragment)
      rlDate.setOnClickListener(this@SetAppointmentFragment)
      tvStartDateTime.setOnClickListener(this@SetAppointmentFragment)
      tvStartDateTimeDisplay.setOnClickListener(this@SetAppointmentFragment)
      tvEndDateTime.setOnClickListener(this@SetAppointmentFragment)
      tvEndDateTimeDisplay.setOnClickListener(this@SetAppointmentFragment)
      btnSave.setOnClickListener(this@SetAppointmentFragment)
      btnReset.setOnClickListener(this@SetAppointmentFragment)
      setCallenderDate()
      AddedAttendeeModel.setAttendeeUserEmplyeeCode(userManager.getUser()!!.employeeCode)
      if (!createMode) {
        tvNewAppointment.text = getString(R.string.txt_label_reschedule_appointment)
      }
    }
  }

  private fun setUpFromArguments() {
    try {
      mUCID = if (this.arguments!!.getString(UCID, "0").equals("", ignoreCase = true))
        "0"
      else
        this.arguments!!.getString(UCID, "0")
      str_LeadID = this.arguments!!.getString(LEAD_ID, "0")
      meetingId = this.arguments!!.getString(MEETING_ID, "0")
      if (meetingId != null && !meetingId.equals("0", ignoreCase = true)) {
        //Editt mode
        createMode = false
        getAppointmentDetailsFromServer()
      } else {
        //Create mode
        selectProduct("")
        activityType("")
        getRepeatMode("")
        selectWeek("")
        selectWeekDay("")
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  private fun selectProduct(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, INTERESTEDIN, null, null
      )
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModel -> renderProduct(uiModel, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun selectWeekDay(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, DAYS,
          null, null
      )
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModel -> renderWeekDay(uiModel, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderProduct(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      arrSelectProduct.clear()
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      arrSelectProduct.clear()
      arrSelectProduct.add(RowData(0, "-1", SELECT_PRODUCT))
      for (i in it.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrSelectProduct.add(RowData(i + 1, lookUpId, lookUpName))
      }

      val arrAdapSelectProduct = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spSelectProduct.adapter = arrAdapSelectProduct
      arrAdapSelectProduct.setSpinnerData(binding.spSelectProduct, arrSelectProduct)

      arrAdapSelectProduct.notifyDataSetChanged()
      arrAdapSelectProduct.setNotifyOnChange(true)

      val productData = getSpinnerRowDataById(id, arrAdapSelectProduct)
      if (productData != null) {
        binding.spSelectProduct.setSelection(productData.position)
      }
      setProductListener()
    }
  }

  private fun setProductListener() {
    disposable.add(
        spSelectProduct.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { xyz ->
              binding.tvselectproducterror.setVisibility(View.GONE)
              productCodeStr = (spSelectProduct.selectedItem as RowData).key
            })
  }

  private fun renderWeekDay(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      arrSelectWeekDay.clear()
      showOnFailurePopup(it)
    }

    val data = uiModel.data
    data?.let {
      arrSelectWeekDay.clear()
      arrSelectWeekDay.add(RowData(0, "-1", SELECT_WEEK_DAYS))
      for (i in it.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrSelectWeekDay.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapSelectWeekDay = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spSelectWeekDay.adapter = arrAdapSelectWeekDay
      arrAdapSelectWeekDay.setSpinnerData(binding.spSelectWeekDay, arrSelectWeekDay)

      arrAdapSelectWeekDay.notifyDataSetChanged()
      val productData = getSpinnerRowDataById(id, arrAdapSelectWeekDay)
      if (productData != null) {
        spSelectWeekDay.setSelection(productData!!.getPosition())
      }
      setWeekDayListener()
    }

  }

  private fun setWeekDayListener() {
    disposable.add(
        spSelectWeekDay.itemSelections<SpinnerAdapter>()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              tvRepeatWeekdayError.visibility = View.GONE
              weekDayCodeStr = (spSelectWeekDay.selectedItem as RowData).key
            }
    )
  }

  private fun activityType(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, ACTIVITYTYPE, null, null
      )
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModel -> renderActivityType(uiModel, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderActivityType(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      arrActivityType.clear()
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      arrActivityType.clear()
      arrActivityType.add(RowData(0, "-1", SELECT_ACTIVITY_TYPE))
      for (i in it.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrActivityType.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapActivityType = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spActivityType.adapter = arrAdapActivityType
      arrAdapActivityType.setSpinnerData(binding.spActivityType, arrActivityType)
      arrAdapActivityType.notifyDataSetChanged()
      val productData = getSpinnerRowDataById(id, arrAdapActivityType)
      if (productData != null) {
        binding.spActivityType.setSelection(productData.position)
      }
      setActivityTypeListener()
    }
  }

  private fun setActivityTypeListener() {
    disposable.add(
        binding.spActivityType.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              activityTypeCodeStr = (binding.spActivityType.selectedItem as RowData).key
              binding.tvActivityTypeError.visibility = View.GONE
            }
    )
  }

  private fun getRepeatMode(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, REPEATMODE, null, null
      )
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModel -> renderRepeatMode(uiModel, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderRepeatMode(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      arrRepeatMode.clear()
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      arrRepeatMode.clear()
      arrRepeatMode.add(RowData(0, "-1", "Repeat On"))
      for (i in it.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrRepeatMode.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapRepeteMode = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spRepeateOn.adapter = arrAdapRepeteMode
      arrAdapRepeteMode.setSpinnerData(binding.spRepeateOn, arrRepeatMode)

      arrAdapRepeteMode.notifyDataSetChanged()
      val productData = getSpinnerRowDataById(id, arrAdapRepeteMode)
      if (productData != null) {
        spRepeateOn.setSelection(productData.position)
      }
      setRepeatOnListener()
    }
  }

  private fun setRepeatOnListener() {
    disposable.add(
        spRepeateOn.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { position ->
              binding.llRepeteView.visibility = if (position > 0) {
                View.VISIBLE
              } else {
                View.GONE
              }
            })
  }

  private fun selectWeek(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, WEEKS,
          null, null
      )
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModel -> renderWeek(uiModel, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderWeek(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      arrSelectWeek.clear()
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      arrSelectWeek.clear()
      arrSelectWeek.add(RowData(0, "-1", SELECT_WEEK))
      for (i in it.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrSelectWeek.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapSelectWeek = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spRepeateWeek.adapter = arrAdapSelectWeek
      arrAdapSelectWeek.setSpinnerData(binding.spRepeateWeek, arrSelectWeek)

      arrAdapSelectWeek.notifyDataSetChanged()
      val productData = getSpinnerRowDataById(id, arrAdapSelectWeek)
      if (productData != null) {
        spRepeateWeek.setSelection(productData.position)
      }
      setRepeatWeekListener()
    }
  }

  private fun setRepeatWeekListener() {
    disposable.add(
        spRepeateWeek.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { xyz ->
              tvRepeatTypeSpError.visibility = View.GONE
              weekCodeStr = (spRepeateWeek.selectedItem as RowData).key
            })
  }

  private fun getAppointmentDetailsFromServer() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = MeetingByIdRequest(meetingId!!)
      val subscribe = saathiApi.getMeetingById(request)
          .compose(applyUiModel())
          .subscribe { this.renderMeetingById(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderMeetingById(uiModel: UiModel<MeetingByIdItem>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    error?.let {
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    if (data != null) {
      val mModel = getAppointmentDet(data)
      setCallenderDateFrom(mModel)
      selectProduct(mModel.getProduct())
      activityType(mModel.getActivityTypeID())
      getRepeatMode(mModel.getRepeatOnCode())
      selectWeek(mModel.getRepeatEveryWeekCode())
      selectWeekDay(mModel.getRepeatEveryDayCode())
    }
  }

  private fun setCallenderDateFrom(mModel: MeetingApptModel) {

    var mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.meetingDate)
    binding.tvApppointmentDateTime.text = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace)
    binding.tvApppointmentDateTimeDisplay.text =
      CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY)
    val cal = Calendar.getInstance()
    cal.time = mMeetingDate
    currentYear = cal.get(Calendar.YEAR)
    month = cal.get(Calendar.MONTH) + 1//Adding one onth
    day = cal.get(Calendar.DAY_OF_MONTH)
    minute = cal.get(Calendar.MINUTE)
    hour = cal.get(Calendar.HOUR_OF_DAY)
    mainFromDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash)

    //Start date
    mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.appointmentStartTime)
    binding.tvStartDateTime.text = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace)
    binding.tvStartDateTimeDisplay.text = CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY)

    cal.time = mMeetingDate
    currentYearStartDate = cal.get(Calendar.YEAR)
    monthStartDate = cal.get(Calendar.MONTH) + 1//Adding one onth
    dayStartDate = cal.get(Calendar.DAY_OF_MONTH)
    minuteStartDate = cal.get(Calendar.MINUTE)
    hourStartDate = cal.get(Calendar.HOUR_OF_DAY)
    mainFromDateStartDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash)
    //end Date
    mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.appointmentEndTime)
    binding.tvEndDateTime.text = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace)
    binding.tvEndDateTimeDisplay.text = CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY)

    cal.time = mMeetingDate
    currentYearEndDate = cal.get(Calendar.YEAR)
    monthEndDate = cal.get(Calendar.MONTH) + 1//Adding one onth
    dayEndDate = cal.get(Calendar.DAY_OF_MONTH)
    minuteEndDate = cal.get(Calendar.MINUTE)
    hourEndDate = cal.get(Calendar.HOUR_OF_DAY)
    mainFromDateEndDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash)
  }

  private fun getAppointmentDet(response: MeetingByIdItem): MeetingApptModel {
    val mModel = MeetingApptModel()
    //        response.get(MEETING_ID);

    mModel.meetingType = stringOrEmpty(response.meetingType)
    mModel.displayType = "A"
    mModel.meetingID = stringOrEmpty(response.meetingID)
    mModel.leadId = response.leadId.toString()
    //TODO LEAD ID IS DIFFRENT
    mModel.meetingDate = stringOrEmpty(response.meetingDate)
    mModel.activityTypeID = stringOrEmpty(response.activityTypeID)
    mModel.product = stringOrEmpty(response.productTypeID)
    mModel.appointmentStartTime = stringOrEmpty(response.repeatationStartOn)
    mModel.appointmentEndTime = stringOrEmpty(response.repeatationEndsOn)

    mModel.repeatEveryWeekCode = stringOrEmpty(response.repeatEveryWeekCode)
    mModel.repeatEveryDayCode = stringOrEmpty(response.repeatEveryDayCode)
    mModel.repeatOnCode = stringOrEmpty(response.repeatOnCode)

    val meetingAttendee = response.meetingAttendee
    for (meetingAttendee_i in meetingAttendee.indices) {
      val meetingAttendee_obj = meetingAttendee[meetingAttendee_i]
      val str_AttendeeType = meetingAttendee_obj?.attendeeType

      val str_AttendeeEmpcode = meetingAttendee_obj?.attendeeEmpcode

      val str_AttendeeEmpName = meetingAttendee_obj?.attendeeEmpName
      val str_meetingAttendeeCode = meetingAttendee_obj?.meetingAttendeeID

      val attended = meetingAttendee_obj?.attended

      val defaultAttendee = meetingAttendee_obj?.defaultAttendee
      val status = meetingAttendee_obj?.status
      if (status == 1) {
        if (!defaultAttendee!!) {
          //TODO ignore self object
          if (str_AttendeeType!!.trim { it <= ' ' }.equals("R", ignoreCase = true)) {
            val attendTmp = AddedAttendeeModel(
                str_AttendeeType, attended!!, str_AttendeeEmpName,
                str_AttendeeEmpcode, str_meetingAttendeeCode
            )
            addedAttendeeModelList.add(attendTmp)
            addedAttendeeCount++
          } else {
            val tempSpAttendee = AddedSplAttendeeModel(
                str_AttendeeType, attended!!,
                str_AttendeeEmpName, str_AttendeeEmpcode
            )
            addedSplAttendeeModelList.add(tempSpAttendee)
            addedSplAttendeeCount++
          }
        }
      }
    }
    binding.tvSplAttendeeCount.text = String.format("(%s)", addedSplAttendeeCount.toString())
    setCount(addedAttendeeCount)
    mModel.meetingAttendee = addedAttendeeModelList
    mModel.splMeetingAttendee = addedSplAttendeeModelList

    return mModel
  }

  override fun onClick(v: View) {
    val id = v.id
    if (id == R.id.ibAttendee) {
      dialogAttendee()
    }
    if (id == R.id.ibSplAttendee) {
      dialogSplAttendee()
    }
    if (id == R.id.tvAttendeeCount) {
      dialogAddedAttendee()
    }
    if (id == R.id.tvSplAttendeeCount) {
      dialogAddedSplAttendee()
    }
    if (id == R.id.tvApppointmentDateTime) {
      selectDateAppointment()
    } else if (id == R.id.tvApppointmentDateTimeDisplay) {
      selectDateAppointment()
    }
    if (id == R.id.rlDate) {
      selectDateAppointment()
    }
    //tvStartDateTime
    if (id == R.id.tvStartDateTime) {
      selectDateAppointmentStartTime()
    } else if (id == R.id.tvStartDateTimeDisplay) {
      selectDateAppointmentStartTime()
    }
    //tvEndDateTime
    if (id == R.id.tvEndDateTime || id == R.id.tvEndDateTimeDisplay) {
      selectDateAppointmentEndTime()
    } else if (id == R.id.btnSave) {

      if (validateForm()) {
        val meetingApptModel = MeetingApptModel()
        meetingApptModel.meetingType = "A"
        meetingApptModel.displayType = "A"
        meetingApptModel.leadId = str_LeadID
        meetingApptModel.ucid = mUCID
        meetingApptModel.meetingDate = CRMAppUtil.getDBDate(currentYear, month, day, hour, minute)
        meetingApptModel.appointmentStartTime =
          CRMAppUtil.getDBDate(
              currentYearStartDate, monthStartDate, dayStartDate,
              hourStartDate, minuteStartDate
          )
        meetingApptModel.appointmentEndTime =
          CRMAppUtil.getDBDate(
              currentYearEndDate, monthEndDate, dayEndDate, hourEndDate,
              minuteEndDate
          )
        meetingApptModel.meetingAttendee = addedAttendeeModelList
        meetingApptModel.splMeetingAttendee = addedSplAttendeeModelList
        meetingApptModel.product = productCodeStr
        //TODO SwitchRepeateOn check
        if (spRepeateOn.selectedItemPosition > 0) {
          val tmp = spRepeateOn.selectedItem
          if (tmp is RowData) {
            val a = tmp
            meetingApptModel.repeatOnCode = a.key
          }
        }

        meetingApptModel.repeatEveryWeekCode = weekCodeStr
        meetingApptModel.repeatEveryDayCode = weekDayCodeStr
        meetingApptModel.activityTypeID = activityTypeCodeStr
        if (!createMode) {
          meetingApptModel.meetingID = meetingId
        }
        createAppointmentSave(meetingApptModel)
      }
    } else if (id == R.id.btnReset) {
      resetForm()
    }//btnReset
  }

  private fun resetForm() {
    binding.apply {
      spSelectProduct.setSelection(0, true)
      spActivityType.setSelection(0, true)
      spRepeateOn.setSelection(0, true)
      spSelectWeekDay.setSelection(0, true)
      spRepeateWeek.setSelection(0, true)
      addedAttendeeModelList.clear()
      tvAttendeeCount.setText("")
      setCallenderDate()
    }

  }

  private fun setCallenderDate() {
    mainFromDate = CRMAppUtil.getDate(Date(), "yyyy-MM-dd")
    try {
      val dateArray = mainFromDate!!.split("-".toRegex())
          .dropLastWhile { it.isEmpty() }
          .toTypedArray()
      currentYear = Integer.parseInt(dateArray[0])
      currentYearStartDate = currentYear
      currentYearEndDate = currentYear
      month = Integer.parseInt(dateArray[1])
      day = Integer.parseInt(dateArray[2])
    } catch (e: Exception) {
      Timber.d(e)
    }
    mainFromDateEndDate = mainFromDate
    mainFromDateStartDate = mainFromDate
    val calendar = Calendar.getInstance()
    hour = calendar.get(Calendar.HOUR_OF_DAY)
    minute = calendar.get(Calendar.MINUTE)
    hourEndDate = hour
    minuteEndDate = minute
    hourStartDate = hour
    minuteStartDate = minute
    binding.apply {
      tvEndDateTime.text = CRMAppUtil.getCurrentDisplayDate()
      tvEndDateTimeDisplay.text = CRMAppUtil.getDBDate(Date(), DATE_FORMAT_TO_DISPLAY)
      tvStartDateTime.text = CRMAppUtil.getCurrentDisplayDate()
      tvStartDateTimeDisplay.text = CRMAppUtil.getDBDate(Date(), DATE_FORMAT_TO_DISPLAY)
      tvApppointmentDateTime.text = CRMAppUtil.getCurrentDisplayDate()

      tvApppointmentDateTimeDisplay.text =
        CRMAppUtil.getDBDate(Date(), DATE_FORMAT_TO_DISPLAY)
    }
  }

  private fun createAppointmentSave(meetingApptModel: MeetingApptModel) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = RequestBody.create(MediaType.parse("text/plain"), meetingApptModel.json)
      val subscribe = saathiApi.setAppointmentMeeting(request)
          .compose(applyUiModel<SetAppointmentResponse>())
          .subscribe { this.renderAddUpdate(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderAddUpdate(uiModel: UiModel<SetAppointmentResponse>) {
    toggleProgress(uiModel.inProgress)
    val error = uiModel.error
    error?.let {
      showOnFailurePopup(it)
    }
    val data = uiModel.data
    data?.let {
      MSFDialog.alertDialog(
          activity, 0,
          resources.getString(R.string.motilal_oswal),
          if (createMode) SET_APPOINTMENTS else UPDATE_APPOINTMENTS,
          "OK", true
      ) { action, data1 ->
        if (action == MSFDialog.Action.OK) {
          val bundle = Bundle()
          bundle.putString(
              LEAD_ID,
              arguments!!.getString(LEAD_ID)
          )
          bundle.putString(
              UCID,
              arguments!!.getString(UCID)
          )
          navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true, false)
        }
      }
    }
  }

  private fun validateForm(): Boolean {
    try {
      val splitstartDate = stringArray(tvStartDateTime.text.toString())
      val newstartDate = splitstartDate.split("-".toRegex())
          .dropLastWhile { it.isEmpty() }
          .toTypedArray()
      val splitEndDate = stringArray(tvEndDateTime.text.toString())
      val newEndDate = splitEndDate.split("-".toRegex())
          .dropLastWhile { it.isEmpty() }
          .toTypedArray()

      val meetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute + 5)
      val NewMeetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute)
      val endDate = CRMAppUtil.getDBDateObj(
          Integer.parseInt(newEndDate[0]),
          Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
          minuteEndDate
      )
      val startDate = CRMAppUtil.getDBDateObj(
          Integer.parseInt(newstartDate[0]),
          Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), hourStartDate,
          minuteStartDate
      )

      //For e
      val startDateEQ = CRMAppUtil.getDBDateObj(
          Integer.parseInt(newstartDate[0]),
          Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), 0, 0
      )
      val endDateEQ = CRMAppUtil.getDBDateObj(
          Integer.parseInt(newEndDate[0]),
          Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), 0, 0
      )


      binding.apply {
        if (spActivityType.selectedItem.toString().equals(SELECT_ACTIVITY_TYPE, ignoreCase = true)
        ) {
          tvActivityTypeError.visibility = View.VISIBLE
          tvActivityTypeError.text = SELECT_ACTIVITY_TYPE
          tvActivityTypeError.requestFocus()
          return false
        } else if (meetingDate.before(Date())) {
          tvAPPDateError.visibility = View.VISIBLE
          tvAPPDateError.text = SELECT_APPT_DATE_TYPE
          tvAPPDateError.requestFocus()
          return false
        } else if (spSelectProduct.selectedItem.toString().equals(SELECT_PRODUCT, ignoreCase = true)
        ) {
          tvselectproducterror.visibility = View.VISIBLE
          tvselectproducterror.text = SELECT_PRODUCT
          tvselectproducterror.requestFocus()
          return false
        }
      }

      //Do this only if you select repete <code></code>
      if (spRepeateOn.selectedItemPosition > 0) {
        if (spRepeateWeek.selectedItem.toString().equals(SELECT_WEEK, ignoreCase = true)) {
          tvRepeatTypeSpError.visibility = View.VISIBLE
          tvRepeatTypeSpError.text = SELECT_WEEK
          tvRepeatTypeSpError.requestFocus()
          return false
        } else if (spSelectWeekDay.selectedItem.toString().equals(
                SELECT_WEEK_DAYS, ignoreCase = true
            )
        ) {
          tvRepeatWeekdayError.visibility = View.VISIBLE
          tvRepeatWeekdayError.text = SELECT_WEEK_DAYS
          tvRepeatWeekdayError.requestFocus()
          return false
        } else if (NewMeetingDate.time > startDate.time) {
          tvStartDateError.visibility = View.VISIBLE
          tvStartDateError.text = START_TIME_ERROR
          tvStartDateError.requestFocus()
          return false
        } else if (startDate.after(endDate)) {
          tvEndDateError.visibility = View.VISIBLE
          tvEndDateError.text = END_TIME_ERROR
          tvEndDateError.requestFocus()
          return false
        } else if (startDateEQ.time == endDateEQ.time) {
          tvEndDateError.visibility = View.VISIBLE
          tvEndDateError.text = END_TIME_EQUAL_ERROR
          tvEndDateError.requestFocus()
          return false
        }
      }

      return true
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }

  }

  @SuppressLint("DefaultLocale")
  private fun selectDateAppointmentStartTime() {
    today = Calendar.getInstance()
    dialogDateTime = Dialog(requireContext()).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(R.layout.date_picker)
      window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    val dpFromDateStartTime = dialogDateTime?.findViewById<DatePicker>(R.id.datePickerFromDate)

    dpFromDateStartTime?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    close = dialogDateTime?.findViewById(R.id.ibtnClose)
    btnSubmit = dialogDateTime?.findViewById(R.id.btnSubmit)

    dpFromDateStartTime?.minDate = System.currentTimeMillis() - 1000
    if (mainFromDateStartDate != null) {
      try {
        val dateSplit = mainFromDateStartDate!!.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        dpFromDateStartTime!!.init(
            Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
            Integer.parseInt(dateSplit[2])
        ) { _, year, monthOfYear, dayOfMonth ->
          mainFromDateStartDate =
            year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
          currentYearStartDate = year
          monthStartDate = monthOfYear + 1
          dayStartDate = dayOfMonth
        }
      } catch (e: Exception) {
        Timber.d(e)
      }

    } else {
      dpFromDateStartTime!!.init(
          today!!.get(Calendar.YEAR), today!!.get(Calendar.MONTH),
          today!!.get(Calendar.DATE)
      ) { _, year, monthOfYear, dayOfMonth ->
        mainFromDateStartDate =
          year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
        currentYearStartDate = year
        monthStartDate = monthOfYear + 1
        dayStartDate = dayOfMonth
      }
    }

    val timePicker = dialogDateTime!!.findViewById<TimePicker>(R.id.timePicker)
        .apply {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = hourStartDate
            minute = minuteStartDate
          } else {
            currentHour = hourStartDate
            currentMinute = minuteStartDate
          }
        }
    timePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

    //Thisgives api level warning
    timePicker.setOnTimeChangedListener { arg0, arg1, arg2 ->
      hourStartDate = arg1
      minuteStartDate = arg2
    }

    close?.setOnClickListener { _ -> dialogDateTime!!.dismiss() }

    btnSubmit?.setOnClickListener { _ ->
      if (mainFromDateStartDate != null) {
        fromDat = CRMAppUtil.getDate(mainFromDateStartDate, "yyyy-MM-dd")
        fromPostingDateStartDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd")
        tvStartDateTime.setText(
            String.format(
                "%s-%d:%d:00", fromPostingDateStartDate, hourStartDate,
                minuteStartDate
            )
        )
      }
      val splitstartDate = stringArray(tvStartDateTime.getText().toString())
      try {
        val newstartDate = splitstartDate.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val splitEndDate = stringArray(tvEndDateTime.getText().toString())
        val newEndDate = splitEndDate.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        tvStartDateTimeDisplay.setText(
            CRMAppUtil.getDBDate(
                CRMAppUtil.getDBDateObj(
                    Integer.parseInt(newstartDate[0]),
                    Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
                    hourStartDate, minuteStartDate
                ), DATE_FORMAT_TO_DISPLAY
            )
        )

        val meetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute)
        val endDate = CRMAppUtil.getDBDateObj(
            Integer.parseInt(newEndDate[0]),
            Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
            minuteEndDate
        )
        val startDate = CRMAppUtil.getDBDateObj(
            Integer.parseInt(newstartDate[0]),
            Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
            hourStartDate, minuteStartDate
        )
        if (startDate.before(endDate)) {
          tvEndDateError.setVisibility(View.GONE)
        }

        if (meetingDate.time > startDate.time) {
          Toast.makeText(activity, START_TIME_ERROR, Toast.LENGTH_SHORT)
              .show()
        } else {
          dialogDateTime!!.dismiss()
          tvStartDateError.visibility = View.GONE
          dialogDateTime!!.dismiss()
        }
      } catch (e: Exception) {
        Timber.d(e, TAG)
      }
    }

    dialogDateTime!!.show()
  }

  @SuppressLint("DefaultLocale")
  private fun selectDateAppointmentEndTime() {
    today = Calendar.getInstance()
    dialogDateTime = Dialog(requireContext()).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(R.layout.date_picker)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    val dpFromDateEndTime = dialogDateTime!!.findViewById<DatePicker>(R.id.datePickerFromDate)

    dpFromDateEndTime.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    close = dialogDateTime!!.findViewById(R.id.ibtnClose)
    btnSubmit = dialogDateTime!!.findViewById(R.id.btnSubmit)

    dpFromDateEndTime.minDate = System.currentTimeMillis() - 1000

    mainFromDateEndDate?.let {
      try {
        val dateSplit = it.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        dpFromDateEndTime.init(
            Integer.parseInt(dateSplit[0]),
            Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2])
        ) { view, year, monthOfYear, dayOfMonth ->
          mainFromDateEndDate =
            year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
          currentYearEndDate = year
          monthEndDate = monthOfYear + 1
          dayEndDate = dayOfMonth
        }
      } catch (e: Exception) {
        Timber.d(e)
      }
    }

    if (mainFromDateEndDate == null) {
      dpFromDateEndTime.init(
          today!!.get(Calendar.YEAR), today!!.get(Calendar.MONTH),
          today!!.get(Calendar.DATE)
      ) { view, year, monthOfYear, dayOfMonth ->
        mainFromDateEndDate =
          year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
        currentYearEndDate = year
        monthEndDate = monthOfYear + 1
        dayEndDate = dayOfMonth
      }

    }

    val timePicker = dialogDateTime!!.findViewById<TimePicker>(R.id.timePicker)
        .apply {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = hourEndDate
            minute = minuteEndDate
          } else {
            currentHour = hourEndDate
            currentMinute = minuteEndDate
          }
        }
    timePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

    timePicker.setOnTimeChangedListener { _, arg1, arg2 ->
      hourEndDate = arg1
      minuteEndDate = arg2
    }

    close?.setOnClickListener { _ -> dialogDateTime?.dismiss() }

    btnSubmit?.setOnClickListener { v ->
      if (mainFromDateEndDate != null) {
        fromDat = CRMAppUtil.getDate(mainFromDateEndDate, "yyyy-MM-dd")
        fromPostingDateEndDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd")
        tvEndDateTime.text = String.format(
            "%s %d:%d:00", fromPostingDateEndDate, hourEndDate,
            minuteEndDate
        )
      }
      val splitstartDate = stringArray(tvStartDateTime.text.toString())
      try {
        val newstartDate = splitstartDate.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val splitEndDate = stringArray(tvEndDateTime.text.toString())
        val newEndDate = splitEndDate.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        tvEndDateTimeDisplay.setText(
            CRMAppUtil.getDBDate(
                CRMAppUtil.getDBDateObj(
                    Integer.parseInt(newEndDate[0]),
                    Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]),
                    hourEndDate, minuteEndDate
                ), DATE_FORMAT_TO_DISPLAY
            )
        )

        val endDate = CRMAppUtil.getDBDateObj(
            Integer.parseInt(newEndDate[0]),
            Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
            minuteEndDate
        )
        val startDate = CRMAppUtil.getDBDateObj(
            Integer.parseInt(newstartDate[0]),
            Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
            hourStartDate, minuteStartDate
        )
        if (startDate.after(endDate)) {
          Toast.makeText(activity, END_TIME_ERROR, Toast.LENGTH_SHORT)
              .show()
        } else {
          dialogDateTime?.dismiss()
          tvEndDateError.visibility = View.GONE
        }
        if (endDate.time > startDate.time) {
          tvEndDateError.visibility = View.GONE
        }
      } catch (e: Exception) {
        Timber.d(TAG)
      }

    }

    dialogDateTime?.show()
  }

  fun prefix(input: Int): String {
    return if (Integer.toString(input).length == 1) {
      "0$input"
    } else input.toString() + ""
  }

  private fun dialogAttendee() {
    val dialogAttendeeList = Dialog(requireContext())
    dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogAttendeeList.window!!
        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogAttendeeList.setContentView(R.layout.dialog_attendee_list)
    dialogAttendeeList.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogAttendeeList.setCanceledOnTouchOutside(true)
    val etSearchAttendee =
      dialogAttendeeList.findViewById<DelayAutoCompleteTextView>(R.id.etSearchAttendee)
    etSearchAttendee.threshold = 3
    etSearchAttendee.imeOptions = EditorInfo.IME_ACTION_DONE
    etSearchAttendee.setOnEditorActionListener(this)
    attendeeAdapter = AttendeeAdapter(context!!)
    etSearchAttendee.setAdapter(attendeeAdapter)
    etSearchAttendee.setLoadingIndicator(
        dialogAttendeeList.findViewById(R.id.pbLoadingIndicator)
    )
    etSearchAttendee.setOnItemClickListener { parent, view, position, id ->

      val clientModel = if (attendeeAdapter!!.count > position)
        attendeeAdapter!!.getItem(position)
      else
        EmployeeModel("", false)
      val has = listHasAttende(
          addedAttendeeModelList, addedSplAttendeeModelList,
          clientModel.attendeeCode
      )
      if (!has) {
        if (!clientModel.isNoData) {
          val attendeeType = "R" //todo //R=Regular  & S=special
          addedAttendeeModelList.add(
              AddedAttendeeModel(
                  attendeeType, true, clientModel.attendeeName,
                  clientModel.attendeeCode, clientModel.meetingAttendeeCode
              )
          )
          addedAttendeeCount++
          setCount(addedAttendeeCount)
          dialogAttendeeList.dismiss()
        }

      } else {
        Toast.makeText(requireContext(), ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT)
            .show()
        etSearchAttendee.setText("")
      }
    }

    dialogAttendeeList.show()
  }

  private fun dialogSplAttendee() {
    val dialogSplAttendeeList = Dialog(requireContext())
    dialogSplAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogSplAttendeeList.window!!
        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogSplAttendeeList.setContentView(R.layout.dialog_attendee_list)
    dialogSplAttendeeList.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogSplAttendeeList.setCanceledOnTouchOutside(true)
    val etSearchAttendee =
      dialogSplAttendeeList.findViewById<DelayAutoCompleteTextView>(R.id.etSearchAttendee)
    etSearchAttendee.threshold = 3
    splAttendeeAdapter = SplAttendeeAdapter(context!!)
    etSearchAttendee.setAdapter(splAttendeeAdapter)
    etSearchAttendee.setLoadingIndicator(
        dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator)
    )
    etSearchAttendee.setOnItemClickListener { _, _, position, _ ->
      val clientModel = if (splAttendeeAdapter!!.count > position)
        splAttendeeAdapter!!.getItem(position)
      else
        SplAttendeeModel("")
      val has = listHasAttende(
          addedAttendeeModelList, addedSplAttendeeModelList,
          clientModel.attendeeCode
      )

      if (!has) {
        if (!clientModel.isNoData) {
          val attendeeType = "S" //todo //R=Regular  & S=special
          addedSplAttendeeModelList.add(
              AddedSplAttendeeModel(
                  attendeeType, true, clientModel.attendeeName,
                  clientModel.attendeeCode
              )
          )
          addedSplAttendeeCount++
          binding.tvSplAttendeeCount.text = "($addedSplAttendeeCount)"
          dialogSplAttendeeList.dismiss()
        }

      } else {
        Toast.makeText(
            requireContext(), SPL_ATTENDEE_ALREADY_ADDED,
            Toast.LENGTH_SHORT
        )
            .show()
        etSearchAttendee.setText("")
      }
    }

    dialogSplAttendeeList.show()
  }

  private fun dialogAddedAttendee() {
    val dialogAddedAttendee = Dialog(requireContext())
    dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogAddedAttendee.window!!
        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list)
    val tvEmpty = dialogAddedAttendee.findViewById<TextView>(R.id.tvEmpty)
    dialogAddedAttendee.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogAddedAttendee.setCanceledOnTouchOutside(true)
    val lvAttendee = dialogAddedAttendee.findViewById<ListView>(R.id.lvAttendee)
    lvAttendee.emptyView = tvEmpty
    val addedAttendeeAdapter = AddedAttendeeAdapter(activity!!, addedAttendeeModelList)
    lvAttendee.adapter = addedAttendeeAdapter
    addedAttendeeAdapter.notifyDataSetChanged()
    dialogAddedAttendee.show()
  }

  private fun dialogAddedSplAttendee() {
    val dialogAddedSplAttendee = Dialog(requireContext())
    dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogAddedSplAttendee.window!!
        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list)
    dialogAddedSplAttendee.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogAddedSplAttendee.setCanceledOnTouchOutside(true)
    val lvAttendee = dialogAddedSplAttendee.findViewById<ListView>(R.id.lvAttendee)
    val addedSplAttendeeAdapter = AddedSplAttendeeAdapter(activity!!, addedSplAttendeeModelList)
    lvAttendee.adapter = addedSplAttendeeAdapter
    addedSplAttendeeAdapter.notifyDataSetChanged()
    dialogAddedSplAttendee.show()
  }

  @SuppressLint("DefaultLocale")
  private fun selectDateAppointment() {
    today = Calendar.getInstance()
    dialogDateTime = Dialog(requireContext()).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(R.layout.date_picker)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    val dpFromDate = dialogDateTime!!.findViewById<DatePicker>(R.id.datePickerFromDate)

    dpFromDate.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

    close = dialogDateTime!!.findViewById<ImageView>(R.id.ibtnClose)
    btnSubmit = dialogDateTime!!.findViewById<Button>(R.id.btnSubmit)
    dpFromDate.minDate = System.currentTimeMillis() - 1000

    mainFromDate?.let {

      try {
        val dateSplit = it.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        dpFromDate.init(
            Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
            Integer.parseInt(dateSplit[2])
        ) { view, year, monthOfYear, dayOfMonth ->
          mainFromDate = year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
          currentYear = year
          month = monthOfYear + 1
          day = dayOfMonth
        }
      } catch (e: Exception) {
        Timber.d(e)
      }

    }

    if (mainFromDate == null) {
      dpFromDate.init(
          today!!.get(Calendar.YEAR), today!!.get(Calendar.MONTH),
          today!!.get(Calendar.DATE)
      ) { view, year, monthOfYear, dayOfMonth ->
        mainFromDate = year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
        currentYear = year
        month = monthOfYear + 1
        day = dayOfMonth
      }
    }

    val timePicker = dialogDateTime!!.findViewById<TimePicker>(R.id.timePicker)
    //    timePic

    timePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      timePicker.hour = hour
      timePicker.minute = minute
    } else {
      timePicker.currentHour = hour
      timePicker.currentMinute = minute
    }
    timePicker.setOnTimeChangedListener { arg0, arg1, arg2 ->
      hour = arg1
      minute = arg2
    }

    close?.setOnClickListener { _ -> dialogDateTime!!.dismiss() }

    btnSubmit?.setOnClickListener { _ ->

      if (mainFromDate != null) {
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd")
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd")
        tvApppointmentDateTime.setText(
            String.format(
                "%s %d:%d:00", fromPostingDate, hour,
                minute
            )
        )//Replaced with  T>> -
        tvAPPDateError.visibility = View.GONE
      }
      val meetingDate = stringArray(tvApppointmentDateTime.getText().toString())
      try {
        val newstartDate = meetingDate.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        tvApppointmentDateTimeDisplay.text = CRMAppUtil.getDBDate(
            CRMAppUtil.getDBDateObj(
                Integer.parseInt(newstartDate[0]),
                Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), hour,
                minute
            ), DATE_FORMAT_TO_DISPLAY
        )//Replaced with  T>> -
      } catch (e: Exception) {
        Timber.d(e)
      }


      dialogDateTime?.dismiss()
      tvStartDateError.setVisibility(View.GONE)
    }

    dialogDateTime?.show()
  }

  private fun stringArray(date: String): String {
    val spaceString = date.split(" ".toRegex())
        .dropLastWhile { it.isEmpty() }
        .toTypedArray()

    return spaceString[0]
  }

  override fun onEditorAction(
    v: TextView?,
    actionId: Int,
    event: KeyEvent?
  ) = false

  private fun setCount(cnt: Int) {
    binding.tvAttendeeCount.text = when {
      cnt <= 0 -> ""
      else -> String.format("(%s)", cnt.toString())
    }
  }

  companion object {
    val TAG = SetAppointmentFragment::class.java.simpleName

    internal fun listHasAttende(
      addedAttendeeModelList: List<AddedAttendeeModel>?,
      addedSplAttendeeModelList: List<AddedSplAttendeeModel>?,
      empID: String
    ): Boolean {
      var has = false
      if (addedAttendeeModelList != null) {
        for (atten in addedAttendeeModelList) {
          if (atten.attendeeEmplyeeCode.trim { it <= ' ' }.equals(
                  empID.trim { it <= ' ' }, ignoreCase = true
              )
          ) {
            has = true
            break
          }
        }
      }
      if (addedSplAttendeeModelList != null) {
        for (atten in addedSplAttendeeModelList) {
          if (atten.attendeeEmplyeeCode.trim { it <= ' ' }.equals(
                  empID.trim { it <= ' ' }, ignoreCase = true
              )
          ) {
            has = true
            break
          }
        }
      }
      return has
    }

  }

  inner class AddedSplAttendeeAdapter internal constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedSplAttendeeModel>
  ) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): Any {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(
      position: Int,
      view1: View,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false)
      val viewBinding = ItemListAddedSplAttendeeBinding.bind(view)
      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = viewBinding.tvAttendeeName
      holder.ivDelete = viewBinding.ivDelete

      holder.tvAttendeeName!!.text = String.format(
          "%s-%s", attendeeModel.attendeeEmplyeeCode,
          attendeeModel.attendeeName
      )
      holder.ivDelete!!.setOnClickListener { _ ->
        addedSplAttendeeCount--
        binding.tvSplAttendeeCount.text = String.format("(%s)", addedSplAttendeeCount.toString())
        attendeeModelList.removeAt(position)
        notifyDataSetChanged()
      }

      return view
    }

    internal inner class Holder {
      var tvAttendeeName: TextView? = null
      var ivDelete: ImageView? = null
    }
  }

  inner class AddedAttendeeAdapter internal constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedAttendeeModel>
  ) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): Any {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(
      position: Int,
      view1: View,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_attendee, parent, false)

      val viewBinding = ItemListAddedAttendeeBinding.bind(view)

      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = viewBinding.tvAttendeeName
      holder.ivDelete = viewBinding.ivDelete

      holder.tvAttendeeName!!.text = String.format(
          "%s-%s", attendeeModel.attendeeEmplyeeCode,
          attendeeModel.attendeeName
      )
      holder.ivDelete!!.setOnClickListener { _ ->
        addedAttendeeCount--
        setCount(addedAttendeeCount)
        attendeeModelList.removeAt(position)
        notifyDataSetChanged()
      }

      return view
    }

    internal inner class Holder {
      var tvAttendeeName: TextView? = null
      var ivDelete: ImageView? = null
    }
  }

  inner class SplAttendeeAdapter internal constructor(internal var mContext: Context) : BaseAdapter(),
      Filterable {
    private var inflater: LayoutInflater? = null
    private var attendeeModelList: List<SplAttendeeModel> = ArrayList()

    init {
      inflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): SplAttendeeModel {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      var convertView = convertView
      var holderView: HolderView? = null
      val clientModel = attendeeModelList[position]
      if (convertView == null) {
        inflater = requireContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        convertView = inflater!!.inflate(R.layout.item_list_attendee, parent, false)
        holderView = HolderView(convertView!!)
        convertView!!.tag = holderView
      } else {
        holderView = convertView.tag as HolderView
      }
      // viewColor(position, convertView);

      viewColor(position, convertView)
      if (clientModel.isNoData) {
        holderView.tvAttendeeName.text = clientModel.attendeeName
      } else {
        holderView.tvAttendeeName.text = String.format(
            "%s-%s-%s", clientModel.attendeeCode,
            clientModel.attendeeName, clientModel.branchCode
        )
      }

      return convertView
    }

    override fun getFilter(): Filter {
      return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
          val filterResults = FilterResults()
          val modelList = ArrayList<SplAttendeeModel>()
          try {
            if (constraint != null) {
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                val request = AutoCompleteAttendeeRequest(
                    URLEncoder.encode(constraint.toString(), "utf-8"),
                    URLEncoder.encode(constraint.toString(), "utf-8")
                )
                try {
                  val dataList = saathiApi.getAutoCompleteSplAttendee(request)
                      .blockingFirst()
                  for (item in dataList) {
                    val employeeName = stringOrEmpty(item.employeeName)
                    val employeeCode = stringOrEmpty(item.employeeCode)
                    val BranchCode = stringOrEmpty(item.branchCode)
                    modelList.add(SplAttendeeModel(employeeName, employeeCode, BranchCode))
                    filterResults.values = modelList
                    filterResults.count = modelList.size
                  }
                } catch (e: Exception) {
                  modelList.add(SplAttendeeModel.getNoData(getErrorOnFailureNew(e)))
                  filterResults.values = modelList
                  filterResults.count = modelList.size
                  return filterResults
                }

              } else {
                noInternetConnectionDialog()
              }
            }
          } catch (ex: Exception) {
            ex.printStackTrace()
          }

          if (filterResults.count == 0) {
            modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE))
            filterResults.values = modelList
            filterResults.count = modelList.size
          }
          return filterResults
        }

        override fun publishResults(
          constraint: CharSequence,
          results: FilterResults?
        ) {
          if (results != null && results.count > 0) {
            attendeeModelList = results.values as List<SplAttendeeModel>
            notifyDataSetChanged()
          } else {
            notifyDataSetInvalidated()
          }
        }
      }
    }

    private fun viewColor(
      position: Int,
      convertView: View
    ) {
      convertView.setBackgroundColor(
          if (position % 2 == 1) {
            Color.parseColor("#F5F5F5")
          } else {
            Color.parseColor("#FFFFFF")
          }
      )
    }

    internal inner class HolderView internal constructor(parent: View) {
      var tvAttendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)

    }
  }

  inner class AttendeeAdapter(internal var context: Context) : BaseAdapter(), Filterable {
    private var inflater: LayoutInflater? = null
    private var resultList: List<EmployeeModel> = ArrayList()

    init {
      this.inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
      return resultList.size
    }

    override fun getItem(index: Int): EmployeeModel {
      return resultList[index]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    override fun getView(
      position: Int,
      convertView1: View?,
      parent: ViewGroup
    ): View {
      var convertView = convertView1
      val holderView: HolderView
      val clientModel = resultList[position]
      if (convertView == null) {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater!!.inflate(R.layout.item_list_attendee, parent, false)
        holderView = HolderView(convertView!!)
        convertView!!.tag = holderView
      } else {
        holderView = convertView.tag as HolderView
      }

      viewColor(position, convertView)
      holderView.tvAttendeeName.text = if (clientModel.isNoData) {
        holderView.linaerLayout.setOnClickListener(null)
        clientModel.attendeeName
      } else {
        String.format(
            "%s-%s-%s", clientModel.attendeeCode, clientModel.attendeeName, clientModel.branchcode
        )
      }
      return convertView
    }

    override fun getFilter(): Filter {
      return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
          val filterResults = FilterResults()
          val modelList = ArrayList<EmployeeModel>()
          try {
            if (constraint != null) {
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                val request = AutoCompleteAttendeeRequest(
                    URLEncoder.encode(constraint.toString(), "utf-8"),
                    URLEncoder.encode(constraint.toString(), "utf-8")
                )
                try {
                  val dataList = saathiApi.getAutoCompleteAttendee(request)
                      .blockingFirst()
                  for (item in dataList) {
                    val employeeName = stringOrEmpty(item.employeeName)
                    val employeeCode = stringOrEmpty(item.employeeCode)
                    val BranchCode = stringOrEmpty(item.branchCode)
                    if (employeeCode.equals(
                            userManager.getUser()!!.employeeCode, ignoreCase = true
                        )
                    ) {
                      continue
                    }
                    modelList.add(
                        EmployeeModel(
                            employeeName, employeeCode,
                            BranchCode, false
                        )
                    )
                    filterResults.values = modelList
                    filterResults.count = modelList.size
                  }
                } catch (e: Exception) {
                  modelList.add(EmployeeModel.getNoData(getErrorOnFailureNew(e)))
                  filterResults.values = modelList
                  filterResults.count = modelList.size
                  return filterResults
                }

              } else {
                noInternetConnectionDialog()
              }
            }
          } catch (ex: Exception) {
            ex.printStackTrace()
          }

          if (filterResults.count == 0) {
            modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE))
            filterResults.values = modelList
            filterResults.count = modelList.size
          }
          return filterResults
        }

        override fun publishResults(
          constraint: CharSequence,
          results: FilterResults?
        ) {
          if (results != null && results.count > 0) {
            resultList = results.values as List<EmployeeModel>
            notifyDataSetChanged()
          } else {
            notifyDataSetInvalidated()
          }
        }
      }
    }

    private fun viewColor(
      position: Int,
      convertView: View
    ) {
      convertView.setBackgroundColor(
          if (position % 2 == 1) {
            Color.parseColor("#F5F5F5")
          } else {
            Color.parseColor("#FFFFFF")
          }
      )
    }

    internal inner class HolderView internal constructor(parent: View) {
      var tvAttendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)
      var linaerLayout: LinearLayout = parent.findViewById(R.id.linaerLayout)
    }
  }

}