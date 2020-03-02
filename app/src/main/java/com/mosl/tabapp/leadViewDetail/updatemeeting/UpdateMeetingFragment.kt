package com.mosl.tabapp.leadViewDetail.updatemeeting

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.common.RowData.getSpinnerRowDataById
import com.mosl.tabapp.constants.MOSLConstants.ACTIONABLES
import com.mosl.tabapp.constants.MOSLConstants.ACTIVITYTYPE
import com.mosl.tabapp.constants.MOSLConstants.BACLIENTMEETING
import com.mosl.tabapp.constants.MOSLConstants.CATEGORY
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN
import com.mosl.tabapp.constants.MOSLConstants.MEETINGDURATION
import com.mosl.tabapp.constants.MOSLConstants.MEETINGREASON
import com.mosl.tabapp.constants.MOSLConstants.OUTSTATIONMEETING
import com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIONABLES
import com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIVITY_TYPE
import com.mosl.tabapp.constants.MOSLConstants.SELECT_BA_CLIENT_MEETING
import com.mosl.tabapp.constants.MOSLConstants.SELECT_CATEGORY
import com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_DURATION
import com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_OBJECTIVE
import com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_REASON
import com.mosl.tabapp.constants.MOSLConstants.SELECT_OUT_STATION_MEETING
import com.mosl.tabapp.constants.MOSLConstants.SELECT_PRODUCT
import com.mosl.tabapp.constants.MOSLConstants.SELECT_SUB_CATEGORY
import com.mosl.tabapp.constants.MOSLConstants.SUBCATEGORY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragUpdateMeetingBinding
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import timber.log.Timber
import java.util.ArrayList
import java.util.Calendar
import javax.inject.Inject

class UpdateMeetingFragment : CommonFragment(), UpdateMeetingView {

  @Inject
  internal lateinit var presenter: UpdateMeetingPresenter

  lateinit var binding: FragUpdateMeetingBinding

  private var meetingDate: String? = null
  private var activityTypeId: String? = null

  private var meetingReasonID: String? = null
  private var actionableID: String? = null
  private var categoryID: String? = null
  private var subCategoryID: String? = null

  private val addedAttendeeModelList = ArrayList<AddedAttendeeModel>()
  private val addedSplAttendeeModelList = ArrayList<AddedSplAttendeeModel>()

  private val arrOutStation = ArrayList<RowData>()
  private val arrSelectProduct = ArrayList<RowData>()
  private val arrMeetingDuration = ArrayList<RowData>()
  private val arrMeetingObjective = ArrayList<RowData>()
  private val arrActivityType = ArrayList<RowData>()
  private val arrMeetingReason = ArrayList<RowData>()
  private val arrCategory = ArrayList<RowData>()
  private val arrSubCategory = ArrayList<RowData>()
  private val arrBAClient = ArrayList<RowData>()
  private val arrActionables = ArrayList<RowData>()
  private var productCodeStr: String? = null
  private var meetingDurationCodeStr: String? = null
  private var activityTypeCodeStr: String? = null
  private var meetingReasonCodeStr: String? = null
  private var actionablesCodeStr: String? = null
  private var categoryCodeStr: String? = null
  private var subCategoryCodestr: String? = null
  private var outStaitionCodeStr: String? = null
  private var baClientCodeStr: String? = null
  private var addedAttendeeCount: Int = 0
  private var addedSplAttendeeCount: Int = 0
  private var currentYear: Int = 0
  private var month: Int = 0
  private var day: Int = 0
  private var hour: Int = 0
  private var minute: Int = 0
  private var profileType: Boolean = false
  private var meetingObjectiveIdStr = "0"

  private var leadId: String? = null
  private var meetingId: String? = null
  private var mUCID = "0"

  private var mandatory = true

  override fun getLayout(): Int {
    return R.layout.frag_update_meeting
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    setAppTitle("Update Meeting")
    binding = FragUpdateMeetingBinding.bind(view)
    setupView()
    setupMember()
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  private fun setupMember() {
    try {
      val screenData =
        arguments?.getParcelable<UpdateMeetingScreenData>(UpdateMeetingView.ARG_UPDATE_MEETING)!!
      meetingId = screenData.meetingId
      leadId = screenData.leadId
      binding.tvProduct.text = "Appointments-${screenData.product}"
      binding.tvMeetingDate.text = screenData.date
      binding.tvMeetingTime.text = "Time-${screenData.time}"
      mUCID = screenData.ucid
      val calendar = Calendar.getInstance()
      hour = calendar.get(Calendar.HOUR_OF_DAY)
      minute = calendar.get(Calendar.MINUTE)
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  private fun setupView() {
    binding.scrollView.apply {
      descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
      isFocusable = true
      isFocusableInTouchMode = true
      setOnTouchListener { v, _ ->
        v.requestFocusFromTouch()
        false
      }
    }
    userManager.getUser()
        ?.let {
          AddedAttendeeModel.setAttendeeUserEmplyeeCode(it.employeeCode)
          if (it.isFranchise() || it.isIFA()) {
            binding.linaerLayoutNew.visibility = View.VISIBLE
            profileType = true
          }
        }
  }

  override fun onDateLayoutClicked(): Observable<Unit> {
    return binding.rlDate.clicks()
  }

  override fun onSaveButtonClicked(): Observable<Unit> {
    return binding.btnSave.clicks()
  }

  override fun onResetButtonClicked(): Observable<Unit> {
    return binding.btnReset.clicks()
  }

  override fun onAttendeeImageButtonClicked(): Observable<Unit> {
    return binding.ibAttendee.clicks()
  }

  override fun onSplAttendeeImageButtonClicked(): Observable<Unit> {
    return binding.ibSplAttendee.clicks()
  }

  override fun onAttendeeCountClicked(): Observable<Unit> {
    return binding.tvAttendeeCount.clicks()
  }

  override fun onSplAttendeeCountClicked(): Observable<Unit> {
    return binding.tvSplAttendeeCount.clicks()
  }

  override fun renderMeetingObjective(uiModel: UiModel<List<Lookup>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }


    uiModel.onSuccess { data ->
      arrMeetingObjective.clear()
      arrMeetingObjective.add(
          RowData(0, "-1", SELECT_MEETING_OBJECTIVE)
      )
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrMeetingObjective.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapMeetingsObjective =
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spMeetingObjective.adapter = arrAdapMeetingsObjective
      arrAdapMeetingsObjective.setSpinnerData<RowData>(
          binding.spMeetingObjective, arrMeetingObjective
      )
      arrAdapMeetingsObjective.notifyDataSetChanged()
      setMeetingObjectiveListener()
    }

  }

  private fun setMeetingObjectiveListener() {
    disposable.add(
        binding.spMeetingObjective.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              meetingObjectiveIdStr = (binding.spMeetingObjective.selectedItem as RowData).key
              binding.tvMeetingObjectiveError.visibility = View.GONE
            }
    )
  }

  override fun renderMeetingById(uiModel: UiModel<Meeting>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { data ->
      meetingDate = stringOrEmpty(data.meetingDate)
      activityTypeId = stringOrEmpty(data.activityTypeID)
      val productTypeID = stringOrEmpty(data.productTypeID)

      meetingReasonID = stringOrEmpty(data.meetingReasonID)
      val meetingDescription = stringOrEmpty(data.meetingDescription)
      val customerRemark = stringOrEmpty(data.customerRemark)
      actionableID = stringOrEmpty(data.actionableID)
      categoryID = stringOrEmpty(data.categoryID)
      val categoryDescription = stringOrEmpty(data.categoryDescription)
      subCategoryID = stringOrEmpty(data.subCategoryID)
      val subCategoryDescription = stringOrEmpty(data.subCategoryDescription)
      val meetingAttendee = data.meetingAttendeeList
      for (attendee in meetingAttendee) {
        val meetingAttendeeID = stringOrEmpty(attendee.meetingAttendeeID)
        val attendeeEmpcode = stringOrEmpty(attendee.attendeeEmpcode)
        val attendeeType = stringOrEmpty(attendee.attendeeType)
        val defaultAttendee = attendee.defaultAttendee
        val attendeeEmpName = stringOrEmpty(attendee.attendeeEmpName)

        if (attendeeType.equals("S", ignoreCase = true)) {
          addedSplAttendeeModelList.add(
              AddedSplAttendeeModel(
                  attendeeType, true,
                  attendeeEmpName, attendeeEmpcode,
                  meetingAttendeeID
              )
          )
          addedSplAttendeeCount++
        } else {
          if (defaultAttendee) {
            addedAttendeeModelList.add(
                AddedAttendeeModel(
                    attendeeType, true,
                    attendeeEmpName, attendeeEmpcode,
                    meetingAttendeeID
                )
            )
            addedAttendeeCount++
          }
        }
      }

      setCountForSpecial(addedSplAttendeeCount)
      setCount(addedAttendeeCount)
      selectProduct(productTypeID)

      try {
        val metadataArray = meetingDate!!.split("T".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val dateArray = metadataArray[0].split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val timeArray = metadataArray[1].split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        currentYear = Integer.parseInt(dateArray[0])
        month = Integer.parseInt(dateArray[1])
        day = Integer.parseInt(dateArray[2])
        hour = Integer.parseInt(timeArray[0])
        minute = Integer.parseInt(timeArray[1])
      } catch (e: Exception) {
        Timber.d(e)
      }

      binding.etMeetingDescription.setText(meetingDescription)
      binding.etCustomerComment.setText(customerRemark)
      binding.etCategory.setText(categoryDescription)
      binding.etSubCategory.setText(subCategoryDescription)
      meetingDate = meetingDate!!.replace("T", " ")
      binding.tvDate.setText(
          CRMAppUtil.getDBDate(
              CRMAppUtil.getDBDateObj(
                  currentYear, month, day, hour,
                  minute
              ), DATE_FORMAT_TO_DISPLAY
          )
      )

    }
  }

  private fun setCountForSpecial(cnt: Int) {
    binding.tvSplAttendeeCount.text = if (cnt <= 0) {
      ""
    } else {
      String.format("(%s)", cnt.toString())
    }
  }

  private fun setCount(cnt: Int) {
    binding.tvAttendeeCount.text = if (cnt <= 0) {
      ""
    } else {
      String.format("(%s)", cnt.toString())
    }
  }

  private fun selectProduct(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      meetingDuration(meetingId!!)
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, INTERESTEDIN, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose<UiModel<List<LookupItem>>>(applyUiModel<List<LookupItem>>())
          .subscribe { uiModelList -> renderProduct(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun meetingDuration(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      activityType(activityTypeId!!)
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!,
          MEETINGDURATION, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderMeetingDuration(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderMeetingDuration(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrMeetingDuration.clear()
      arrMeetingDuration.add(
          RowData(0, "-1", SELECT_MEETING_DURATION)
      )
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrMeetingDuration.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapMeetingDuration =
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spMeetingDuration.setAdapter(arrAdapMeetingDuration)
      arrAdapMeetingDuration.setSpinnerData<RowData>(binding.spMeetingDuration, arrMeetingDuration)
      arrAdapMeetingDuration.notifyDataSetChanged()
      val meetingDurationData = getSpinnerRowDataById(id, arrAdapMeetingDuration)
      if (meetingDurationData != null) {
        binding.spMeetingDuration.setSelection(
            meetingDurationData.position
        )
      }

      setMeetingDuartionListener()
    }
  }

  private fun setMeetingDuartionListener() {
    disposable.add(
        binding.spMeetingDuration.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { xyz ->
              meetingDurationCodeStr = (binding.spMeetingDuration.selectedItem as RowData).key
              binding.tvMeetingDurationError.visibility = View.GONE
            }
    )
  }

  private fun renderProduct(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrSelectProduct.clear()
      arrSelectProduct.add(RowData(0, "-1", SELECT_PRODUCT))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrSelectProduct.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapSelectProduct = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spProduct.adapter = arrAdapSelectProduct
      arrAdapSelectProduct.setSpinnerData<RowData>(binding.spProduct, arrSelectProduct)
      arrAdapSelectProduct.notifyDataSetChanged()
      val productData = getSpinnerRowDataById(id, arrAdapSelectProduct)
      if (productData != null) {
        binding.spProduct.setSelection(productData!!.position)
      }

      setProductListener()
    }
  }

  private fun setProductListener() {
    disposable.add(
        binding.spProduct.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { xyz ->
              productCodeStr = (binding.spProduct.selectedItem as RowData).key
              binding.tvProductError.visibility = View.GONE
            }
    )
  }

  private fun activityType(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      actionables(actionableID!!)
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, ACTIVITYTYPE, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderActivityType(uiModelList, id) }
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
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrActivityType.clear()
      arrActivityType.add(RowData(0, "-1", SELECT_ACTIVITY_TYPE))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrActivityType.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapActivityType = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spActivityType.setAdapter(arrAdapActivityType)
      arrAdapActivityType.setSpinnerData<RowData>(binding.spActivityType, arrActivityType)
      arrAdapActivityType.notifyDataSetChanged()
      val activityTypeData = getSpinnerRowDataById(id, arrAdapActivityType)
      if (activityTypeData != null) {
        binding.spActivityType.setSelection(
            activityTypeData.position
        )
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
              if (!binding.spActivityType.selectedItem
                      .toString()
                      .equals(SELECT_ACTIVITY_TYPE, ignoreCase = true)
              ) {
                meetingReason(meetingReasonID!!)
              }
            }
    )
  }

  private fun meetingReason(id: String) {

    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      baClient()
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!,
          MEETINGREASON,
          activityTypeCodeStr, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderMeetingReason(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderMeetingReason(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrMeetingReason.clear()
      arrMeetingReason.add(
          RowData(0, "-1", SELECT_MEETING_REASON)
      )
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrMeetingReason.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapMeetingReason = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spMeetingReason.adapter = arrAdapMeetingReason
      arrAdapMeetingReason.setSpinnerData<RowData>(binding.spMeetingReason, arrMeetingReason)
      arrAdapMeetingReason.notifyDataSetChanged()
      val meetingReasonData = getSpinnerRowDataById(id, arrAdapMeetingReason)
      if (meetingReasonData != null) {
        binding.spMeetingReason.setSelection(
            meetingReasonData.position
        )
      }
      setMeetingReasonListener()
    }
  }

  private fun setMeetingReasonListener() {
    disposable.add(
        binding.spMeetingReason.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              meetingReasonCodeStr = (binding.spMeetingReason.getSelectedItem() as RowData).key
              val meetingReasonNameStr =
                (binding.spMeetingReason.selectedItem as RowData).value
              binding.tvMeetingReasonError.visibility = View.GONE
              if (meetingReasonNameStr.equals("Customer Service Related", ignoreCase = true)) {
                binding.llActionables.visibility = View.VISIBLE
              } else {
                binding.llActionables.visibility = View.GONE
              }
            }
    )
  }

  private fun baClient() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      outStation()
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!,
          BACLIENTMEETING, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { this.renderBaClient(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderBaClient(uiModel: UiModel<List<LookupItem>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrBAClient.clear()
      arrBAClient.add(RowData(0, "-1", SELECT_BA_CLIENT_MEETING))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrBAClient.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapBAClient = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spBAClient.adapter = arrAdapBAClient
      arrAdapBAClient.setSpinnerData<RowData>(binding.spBAClient, arrBAClient)
      arrAdapBAClient.notifyDataSetChanged()
      setBAClientListener()
    }
  }

  private fun setBAClientListener() {
    disposable.add(
        binding.spBAClient.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              baClientCodeStr = (binding.spBAClient.selectedItem as RowData).key
              binding.tvBAClientError.visibility = View.GONE
            }
    )
  }

  private fun outStation() {

    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!,
          OUTSTATIONMEETING, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { this.renderOutStation(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderOutStation(uiModel: UiModel<List<LookupItem>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrOutStation.clear()
      arrOutStation.add(RowData(0, "-1", SELECT_OUT_STATION_MEETING))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrOutStation.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapOutStation = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spOutStaition.adapter = arrAdapOutStation
      arrAdapOutStation.setSpinnerData<RowData>(binding.spOutStaition, arrOutStation)

      arrAdapOutStation.notifyDataSetChanged()
      setOutStationListener()
    }
  }

  private fun setOutStationListener() {
    disposable.add(
        binding.spOutStaition.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              outStaitionCodeStr = (binding.spOutStaition.getSelectedItem() as RowData).key
              binding.tvOutStaitionError.visibility = View.GONE
            }
    )
  }

  private fun actionables(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      category(categoryID!!)
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, ACTIONABLES, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderActionables(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderActionables(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrActionables.clear()
      arrActionables.add(RowData(0, "-1", SELECT_ACTIONABLES))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrActionables.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }

      val arrAdapActionables = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spActionables.setAdapter(arrAdapActionables)
      arrAdapActionables.setSpinnerData<RowData>(binding.spActionables, arrActionables)
      arrAdapActionables.notifyDataSetChanged()
      val actionablesData = getSpinnerRowDataById(id, arrAdapActionables)
      if (actionablesData != null) {
        binding.spActionables.setSelection(
            actionablesData.position
        )
      }
      setActionablesListener()
    }
  }

  private fun setActionablesListener() {
    disposable.add(
        binding.spActionables.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              actionablesCodeStr = (binding.spActionables.getSelectedItem() as RowData).key
              binding.tvActionablesError.visibility = View.GONE
            }
    )
  }

  private fun category(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, CATEGORY, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderCategory(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderCategory(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrCategory.clear()
      arrCategory.add(RowData(0, "-1", SELECT_CATEGORY))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrCategory.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapCategory = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spCategory.adapter = arrAdapCategory
      arrAdapCategory.setSpinnerData<RowData>(binding.spCategory, arrCategory)
      arrAdapCategory.notifyDataSetChanged()
      val categoryData = getSpinnerRowDataById(id, arrAdapCategory)
      if (categoryData != null) {
        binding.spCategory.setSelection(categoryData.position)
      }
      setCategoryListener()
    }
  }

  private fun setCategoryListener() {
    disposable.add(
        binding.spCategory.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe { xyz ->
              categoryCodeStr = (binding.spCategory.selectedItem as RowData).key
              binding.tvCategoryError.visibility = View.GONE
              subCategory(subCategoryID!!)
            }
    )
  }

  private fun subCategory(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, SUBCATEGORY,
          categoryCodeStr,
          null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { uiModelList -> renderSubcategory(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderSubcategory(
    uiModel: UiModel<List<LookupItem>>,
    id: String
  ) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrSubCategory.clear()
      arrSubCategory.add(RowData(0, "-1", SELECT_SUB_CATEGORY))
      mandatory = data.size > 1
      if (mandatory) {
        binding.llSubcategory.visibility = View.VISIBLE
      } else {
        binding.etSubCategory.setText("")
        binding.llSubcategory.visibility = View.GONE
      }
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrSubCategory.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      val arrAdapSubCategory = SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
      binding.spSubCategory.adapter = arrAdapSubCategory
      arrAdapSubCategory.setSpinnerData<RowData>(binding.spSubCategory, arrSubCategory)
      arrAdapSubCategory.notifyDataSetChanged()
      val subCategoryData = getSpinnerRowDataById(id, arrAdapSubCategory)
      if (subCategoryData != null) {
        binding.spSubCategory.setSelection(
            subCategoryData.position
        )
      }
      setSubCategoryListener()
    }
  }

  private fun setSubCategoryListener() {
    disposable.add(
        binding.spSubCategory.itemSelections()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe {
              subCategoryCodestr = (binding.spSubCategory.selectedItem as RowData).key
              binding.tvSubCategoryError.visibility = View.GONE
            }
    )
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}