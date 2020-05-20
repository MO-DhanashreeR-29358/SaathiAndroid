package com.mosl.tabapp.leadViewDetail.createmeeting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragCreateMeetingBinding
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.AttendeeItemClick
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class CreateMeetingFragmentNew : CommonFragment(), CreateMeetingView {
  lateinit var binding: FragCreateMeetingBinding
  lateinit var attendeeAdapter: AttendeeAdapter

  @Inject
  lateinit var presenter: CreateMeetingPresenter
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

  private val subject = PublishSubject.create<CreateMeetingUiEvent>()

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
  }

  private fun setupViews() {
    productAdapter = SpinnerAdapterFactory.getDefaultAdapter<LookUp>(requireContext())
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

    if (userManager.getUser()!!
            .isFranchise() || userManager.getUser()!!
            .isIFA()
    ) {
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
  var pbLoadingIndicator: ProgressBar? = null

  override fun getSearchText(): Observable<CharSequence> {
    return etSearchAttendeeText.textChanges()
  }

  override fun showAttendeDialog() {
    val dialog = Dialog(requireContext())
    dialog.apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(R.layout.dialog_attendee_list)
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      setCanceledOnTouchOutside(true)
    }
    etSearchAttendeeText = dialog.findViewById(R.id.etSearchAttendee)
    etSearchAttendeeText?.imeOptions = EditorInfo.IME_ACTION_DONE
    etSearchAttendeeText?.threshold = 3
    attendeeAdapter = AttendeeAdapter(requireContext())
    etSearchAttendeeText?.setAdapter(attendeeAdapter)
    pbLoadingIndicator = dialog.findViewById(R.id.pbLoadingIndicator)
    etSearchAttendeeText?.setLoadingIndicator(pbLoadingIndicator)
    etSearchAttendeeText.setOnItemClickListener { _, _, position, _ ->
      subject.onNext(AttendeeItemClick(position))
    }
    dialog.show()
  }

  override fun onAttendeeItemClickEventFired(): Observable<AttendeeItemClick> = subject.ofType()

  override fun renderOnAttendeeItemClick(position: Int) {
    val clientModel = if (attendeeAdapter.count > position) {
      attendeeAdapter.getItem(position)
    } else {
      TODO()
    }
    TODO()
  }

  override fun toggleSearchState(queryEmpty: Boolean) {
    if (queryEmpty) {
      attendeeAdapter.setResultList(emptyList())
    }
  }

  override fun renderSearchResult(uiModel: UiModel<List<Attendee>>) {
    toggleSearchProgress(uiModel.inProgress)

    uiModel.onFailure {
      Timber.d(uiModel.error)
      attendeeAdapter.notifyDataSetInvalidated()
    }

    uiModel.onSuccess {
      attendeeAdapter.setResultList(it)
      if (it.isNotEmpty()) {
        etSearchAttendeeText?.showDropDown()
      }
    }
  }

  fun toggleSearchProgress(show: Boolean) {
    if (show) {
      pbLoadingIndicator?.visibility = View.VISIBLE
    } else {
      pbLoadingIndicator?.visibility = View.GONE
    }
  }

  override fun showSplAttendeDialog() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  fun onSendOTPClicked(): Observable<Unit> {
    return binding.btnSendOtp.clicks()
  }

  override fun onAttendeeClicked(): Observable<Unit> {
    return binding.ibAttendee.clicks()
  }

  override fun onSplAttendeeClicked(): Observable<Unit> {
    return binding.ibSplAttendee.clicks()
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
    if (userManager.getUser()!!
            .isRM()
    ) {
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

  companion object {
    const val ARG_LEAD_ID = "LeadID"
    const val ARG_UCID = "UCID"
  }
}