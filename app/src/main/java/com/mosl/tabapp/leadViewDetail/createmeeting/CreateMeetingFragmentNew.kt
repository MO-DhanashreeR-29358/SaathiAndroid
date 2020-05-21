package com.mosl.tabapp.leadViewDetail.createmeeting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.pdf.OTPFragment
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMMOSLCommon
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragCreateMeetingBinding
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants.hasAttendeeAdded
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.AttendeeItemClick
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SearchTextChange
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SearchTextChangeSplAttendee
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SplAttendeeItemClick
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class CreateMeetingFragmentNew : CommonFragment(), CreateMeetingView, OTPFragment.OTPDialogListener,
    FusedLocation.LatLongDispatcher, AddedAttendeeAdapter.ClickListenerHandler {
  lateinit var binding: FragCreateMeetingBinding
  lateinit var attendeeAdapter: AttendeeAdapter
  lateinit var addedAttendeeAdapter: AddedAttendeeAdapter
  lateinit var splAttendeeAdapter: AttendeeAdapter
  lateinit var addedSplAttendeeAdapter: AddedAttendeeAdapter
  @Inject
  lateinit var presenter: CreateMeetingPresenter
  @Inject
  lateinit var permissionCheckHelper: PermissionCheckHelperFragment
  @Inject
  lateinit var fusedLocation: FusedLocation
  lateinit var productAdapter: ArrayAdapter<LookUp>
  lateinit var categoryAdapter: ArrayAdapter<LookUp>
  lateinit var subCategoryAdapter: ArrayAdapter<LookUp>
  lateinit var actionableAdapter: ArrayAdapter<LookUp>
  lateinit var activityAdapter: ArrayAdapter<LookUp>
  lateinit var meetingObjectiveAdapter: ArrayAdapter<LookUp>
  lateinit var meetingDurationAdapter: ArrayAdapter<LookUp>
  lateinit var meetingLocationAdapter: ArrayAdapter<LookUp>
  lateinit var meetingReasonAdapter: ArrayAdapter<LookUp>
  lateinit var travellingModeAdapter: ArrayAdapter<LookUp>
  val subject: PublishSubject<String> = PublishSubject.create()

  private val uiSubject = PublishSubject.create<CreateMeetingUiEvent>()

  override fun getLayout() = R.layout.frag_create_meeting

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragCreateMeetingBinding.bind(view)
    setAppTitle(R.string.page_title_create_meeting)
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

    if (userManager.getUser()!!.isFranchise() || userManager.getUser()!!.isIFA()) {
      binding.linaerLayoutNew.visibility = View.VISIBLE
    }
  }

  override fun render(uiModel: UiModel<ClientData>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      val clientDetail = it.clientDetail
      binding.layoutTopSection.tvLeadId.setText(clientDetail.leadId.toString())
      binding.layoutTopSection.tvCurrentStage.setText(clientDetail.currentStage)
      if (clientDetail.currentStageSince.isNotEmpty()) {
        binding.layoutTopSection.tvCurrentStageSince.setText(
            String.format("%s Days", clientDetail.currentStageSince)
        )
      }
      binding.layoutTopSection.tvName.setText(clientDetail.name)
      binding.layoutTopSection.tvClientCode.setText(clientDetail.clientcode)
      binding.layoutTopSection.tvBranchCode.setText(clientDetail.branchcode)
      val imageUrl = Base64.decode(it.imageUrl, Base64.DEFAULT)
      if (imageUrl != null) {
        CRMAppUtil.setImage(imageUrl, binding.layoutTopSection.ivLead, requireContext())
      }
      binding.tvDate.setText(CRMAppUtil.getDBDate(Date(), DATE_FORMAT_TO_DISPLAY))
      productAdapter.setSpinnerData(binding.spProduct, it.listInterestedIn)
      meetingObjectiveAdapter.setSpinnerData(binding.spMeetingObjective, it.listMeetingObjective)
      meetingLocationAdapter.setSpinnerData(binding.spMeetingLocation, it.listMeetingAdressID)
      activityAdapter.setSpinnerData(binding.spActivityType, it.listActivity)
      meetingDurationAdapter.setSpinnerData(binding.spMeetingDuration, it.listMeetingDuration)
      travellingModeAdapter.setSpinnerData(binding.spTravellingMode, it.listTravellingMode)
    }
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
      subCategoryAdapter.clear()
      subCategoryAdapter.setSpinnerData(binding.spSubCategory, it)
    }
  }

  override fun renderCustomerServiceData(uiModel: UiModel<Pair<List<LookUp>, List<LookUp>>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)

      uiModel.onSuccess {
        binding.llActionables.visibility = View.VISIBLE
        actionableAdapter.setSpinnerData(binding.spActionables, it.second)
        categoryAdapter.setSpinnerData(binding.spCategory, it.first)
      }
    }
  }

  lateinit var etSearchAttendeeText: DelayAutoCompleteTextView
  lateinit var etSearchSplAttendeeText: DelayAutoCompleteTextView
  var pbLoadingIndicator: ProgressBar? = null
  var pbLoadingIndicator1: ProgressBar? = null

  override fun getAttendeeSearchText(): Observable<SearchTextChange> {
    return uiSubject.ofType()
  }

  override fun getSplAttendeeSearchText(): Observable<SearchTextChangeSplAttendee> {
    return uiSubject.ofType()
  }

  lateinit var attendeeDialog: Dialog
  lateinit var splAttendeeDialog: Dialog
  override fun showAttendeDialog() {
    attendeeDialog = Dialog(requireContext())
    attendeeDialog.apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(R.layout.dialog_attendee_list)
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
      setContentView(R.layout.dialog_attendee_list)
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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

  var addedAttendeeCount = 0
  var addedSplAttendeeCount = 0
  val addedAttendeeList: ArrayList<AddedAttendee> = ArrayList()
  val addedSplAttendeeList: ArrayList<AddedAttendee> = ArrayList()
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

  fun setCount(
    count: Int,
    textView: TextView
  ) {
    if (count <= 0) {
      textView.setText("")
    } else {
      textView.setText(String.format("(%s)", count.toString()))
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
      Timber.d(uiModel.error)
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

  fun toggleSearchProgress(
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

  fun showAddedDialog(isAttendee: Boolean) {
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

  override fun getGeoArea(): String {
//    val geocoder = Geocoder(requireContext(), Locale.getDefault())
//    val addressLine = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
//        return addressLine.get(0).getAddressLine(0)
    return "kandivli"
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

  override fun toggleOTPVisibility(show: Boolean) {
    if (userManager.getUser()!!.isRM()) {
      if (show) {
        binding.btnSendOtp.visibility = View.VISIBLE
      } else {
        binding.btnSendOtp.visibility = View.GONE
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
        subject.onNext("OTP")
      } else {
        fusedLocation.LocationUpdates()
      }
    }
  }

  override fun getOTPSubject(): Observable<String> {
    return subject.ofType()
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

  fun checkPermission(): Boolean {
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

  var isSendOTPSuccesssful = false
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

  companion object {
    const val ARG_LEAD_ID = "LeadID"
    const val ARG_UCID = "UCID"
    const val SEND_OTP_LABEL = "Send OTP"
    const val VERIFY_OTP_LABEL = "Verify OTP"
  }
}