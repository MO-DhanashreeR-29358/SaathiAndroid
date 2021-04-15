package com.mosl.tabapp.leadViewDetail.updatemeeting

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.common.RowData.getSpinnerRowDataById
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogAddeddAttendeeListBinding
import com.mosl.tabapp.databinding.DialogAddeddSplAttendeeListBinding
import com.mosl.tabapp.databinding.DialogAttendeeListBinding
import com.mosl.tabapp.databinding.FragUpdateMeetingBinding
import com.mosl.tabapp.model.*
import com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
import com.mosl.tabapp.setappointment.SetAppointmentFragment.Companion.listHasAttende
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import timber.log.Timber
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

class UpdateMeetingFragment : CommonFragment(), UpdateMeetingView, OnEditorActionListener {

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

  private val arrSelectProduct = ArrayList<RowData>()
  private val arrMeetingDuration = ArrayList<RowData>()
  private val arrMeetingObjective = ArrayList<RowData>()
  private val arrActivityType = ArrayList<RowData>()
  private val arrMeetingReason = ArrayList<RowData>()
  private val arrCategory = ArrayList<RowData>()
  private val arrSubCategory = ArrayList<RowData>()
  private val arrActionables = ArrayList<RowData>()
  private var productCodeStr: String? = null
  private var meetingDurationCodeStr: String? = null
  private var activityTypeCodeStr: String? = null
  private var meetingReasonCodeStr: String? = null
  private var actionablesCodeStr: String? = null
  private var categoryCodeStr: String? = null
  private var subCategoryCodestr: String? = null
  private var addedAttendeeCount: Int = 0
  private var addedSplAttendeeCount: Int = 0
  private var currentYear: Int = 0
  private var month: Int = 0
  private var day: Int = 0
  private var hour: Int = 0
  private var minute: Int = 0
  private var meetingObjectiveIdStr = "0"

  private var attendeeAdapter: AttendeeAdapter? = null
  private var splAttendeeAdapter: SplAttendeeAdapter? = null

  private var argumentData: UpdateMeetingScreenData? = null

  private var leadId: String? = "0"
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
  }

  override fun onEditorAction(
    view: TextView,
    actionId: Int,
    event: KeyEvent
  ): Boolean {
    if (view is DelayAutoCompleteTextView) {
      hideKeyboard(view)
      return false
    }
    when (view.id) {
      R.id.etMeetingDescription -> binding.etCustomerComment.requestFocus()
      R.id.etCustomerComment -> {
        if (binding.llActionables.visibility == View.VISIBLE)
          binding.etCategory.requestFocus()
      }

      R.id.etCategory -> binding.etSubCategory.requestFocus()

    }
    return false
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

  override fun onCategoryTextChanged(): Observable<String> {
    return binding.etCategory.afterTextChangeEvents()
        .map {
          it.editable?.toString()
        }
  }

  override fun onSubCategoryTextChanged(): Observable<String> {
    return binding.etSubCategory.afterTextChangeEvents()
        .map {
          it.editable?.toString()
        }
  }

  override fun onMeetingDescriptionTextChanged(): Observable<String> {
    return binding.etMeetingDescription.afterTextChangeEvents()
        .map {
          it.editable?.toString()
        }
  }

  override fun renderCategoryTextChanged(text: String) {
    when {
      binding.etCategory.text.toString().isNotBlank() &&
          (binding.etCategory.text.toString().length < 10) -> binding.etCategory.error =
        ATLEAST_TEN_CHARACTERS

    }
  }

  override fun renderSubCategoryTextChanged(text: String) {
    when {
      binding.etSubCategory.text.toString().isNotBlank() &&
          (binding.etSubCategory.text.toString().length < 10) -> binding.etSubCategory.error =
        ATLEAST_TEN_CHARACTERS

    }
  }

  override fun renderMeetingDescriptionTextChanged(text: String) {
    when {
      binding.etMeetingDescription.text.toString().isNotBlank() &&
          (binding.etMeetingDescription.text.toString().length < 10) -> binding.etMeetingDescription.error =
        ATLEAST_TEN_CHARACTERS

    }
  }

  override fun renderAttendeeImageButtonClick() {
    attendeeDialog()
  }

  private fun attendeeDialog() {
    val dialog = Dialog(requireContext()).apply {
      val viewBinding =
        DialogAttendeeListBinding.bind(layoutInflater.inflate(R.layout.dialog_attendee_list, null))
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(viewBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      setCanceledOnTouchOutside(true)
      viewBinding.etSearchAttendee.apply {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setOnEditorActionListener(this@UpdateMeetingFragment)
        threshold = 3
        attendeeAdapter = AttendeeAdapter(requireContext())
        setAdapter(attendeeAdapter)
        setLoadingIndicator(viewBinding.pbLoadingIndicator)
        setOnItemClickListener { _, _, position, _ ->

          val adapter = attendeeAdapter

          val clientModel =
            if (adapter!!.count > position) adapter!!.getItem(position) else EmployeeModel(
                "", false
            )
          val has = listHasAttende(
              addedAttendeeModelList, addedSplAttendeeModelList, clientModel.attendeeCode
          )

          if (!has) {
            if (!clientModel.isNoData) {

              val attendeeType = "R" //todo //R=Regular  & S=special
              addedAttendeeModelList.add(
                  AddedAttendeeModel(
                      attendeeType, true, clientModel.attendeeName,
                      clientModel.attendeeCode
                  )
              )
              addedAttendeeCount++
              binding.tvAttendeeCount.text = if (addedAttendeeCount <= 0) "" else
                String.format("(%s)", addedAttendeeModelList.size.toString())
              dismiss()
            }
          } else {
            Toast.makeText(
                requireContext(), ATTENDEE_ALREADY_ADDED,
                Toast.LENGTH_SHORT
            )
                .show()
            viewBinding.etSearchAttendee.setText("")
          }

        }

      }

    }
    dialog.show()
  }

  override fun renderSplAttendeeImageButtonClick() {
    splAttendeeDialog()
  }

  private fun splAttendeeDialog() {
    val dialog = Dialog(requireContext()).apply {

      val viewBinding =
        DialogAttendeeListBinding.bind(layoutInflater.inflate(R.layout.dialog_attendee_list, null))

      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(viewBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      setCanceledOnTouchOutside(true)
      viewBinding.etSearchAttendee.imeOptions = EditorInfo.IME_ACTION_DONE
      viewBinding.etSearchAttendee.setOnEditorActionListener(this@UpdateMeetingFragment)
      viewBinding.etSearchAttendee.threshold = 3
      splAttendeeAdapter = SplAttendeeAdapter(requireContext())
      viewBinding.etSearchAttendee.setAdapter(splAttendeeAdapter)
      viewBinding.etSearchAttendee.setLoadingIndicator(
          viewBinding.pbLoadingIndicator
      )
      viewBinding.etSearchAttendee.setOnItemClickListener { _, view, position, id ->

        val splAdapter = splAttendeeAdapter

        val clientModel =
          if (splAdapter!!.count > position) splAdapter.getItem(position) else SplAttendeeModel("")
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
            binding.tvSplAttendeeCount.text =
              String.format("(%s)", addedSplAttendeeModelList.size.toString())
            dismiss()
          }
        } else {
          Toast.makeText(
              requireContext(), SPL_ATTENDEE_ALREADY_ADDED,
              Toast.LENGTH_SHORT
          )
              .show()
          viewBinding.etSearchAttendee.setText("")
        }
      }
    }
    dialog.show()
  }

  override fun renderAttendeeCountClick() {
    addedAttendeeDialog()
  }

  private fun addedAttendeeDialog() {
    val dialogAddedAttendee = Dialog(requireContext()).apply {
      val viewBinding = DialogAddeddAttendeeListBinding.bind(
          layoutInflater.inflate(R.layout.dialog_addedd_attendee_list, null)
      )
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(viewBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      setCanceledOnTouchOutside(true)
      viewBinding.lvAttendee.emptyView = viewBinding.tvEmpty
      val addedAttendeeAdapter = AddedAttendeeAdapter(requireContext(), addedAttendeeModelList)
      viewBinding.lvAttendee.adapter = addedAttendeeAdapter
      addedAttendeeAdapter.notifyDataSetChanged()
    }

    dialogAddedAttendee.show()
  }

  override fun renderSplAttendeeCountClick() {
    addedSplAttendeeDialog()
  }

  private fun addedSplAttendeeDialog() {
    val dialogAddedSplAttendee = Dialog(requireContext()).apply {

      val viewBinding = DialogAddeddSplAttendeeListBinding.bind(
          layoutInflater.inflate(R.layout.dialog_addedd_spl_attendee_list, null)
      )

      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setContentView(viewBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      setCanceledOnTouchOutside(true)
      viewBinding.lvAttendee.emptyView = viewBinding.tvEmpty
      val addedSplAttendeeAdapter =
        AddedSplAttendeeAdapter(requireContext(), addedSplAttendeeModelList)
      viewBinding.lvAttendee.adapter = addedSplAttendeeAdapter
      addedSplAttendeeAdapter.notifyDataSetChanged()
    }

    dialogAddedSplAttendee.show()
  }

  override fun renderResetButtonClick() {
    addedAttendeeModelList.clear()
    addedSplAttendeeModelList.clear()
    binding.apply {
      tvAttendeeCount.text = ""
      tvSplAttendeeCount.text = ""
      tvDate.text = meetingDate
      spProduct.setSelection(0)
      spMeetingDuration.setSelection(0)
      spActivityType.setSelection(0)
      spMeetingReason.setSelection(0)
      etMeetingDescription.setText("")
      etCustomerComment.setText("")
      spActionables.setSelection(0)
      spCategory.setSelection(0)
      etCategory.setText("")
      spSubCategory.setSelection(0)
      etSubCategory.setText("")
    }

  }

  override fun renderSaveButtonClick() {
    if (validateForm()) {
      val meetingApptModel = MeetingApptModel().apply {
        meetingType = "M"
        displayType = "U"
        leadId = this@UpdateMeetingFragment.leadId ?: "0"
        ucid = mUCID
        meetingID = meetingId ?: "0"
        meetingDate = CRMAppUtil.getDBDate(currentYear, month, day, hour, minute)
        product = productCodeStr
        meetingDuration = meetingDurationCodeStr
        meetingObjectiveID = meetingObjectiveIdStr
        activityTypeID = activityTypeCodeStr
        meetingReason = meetingReasonCodeStr
        meetingDes =
          getJsonSafeString(binding.etMeetingDescription.text.toString().trim { it <= ' ' })
        meetingAttendee = addedAttendeeModelList
        splMeetingAttendee = addedSplAttendeeModelList
        customerComment =
          getJsonSafeString(binding.etCustomerComment.text.toString().trim { it <= ' ' })
        actionables = actionablesCodeStr
        category = categoryCodeStr
        etCategory = binding.etCategory.text.toString()
            .trim { it <= ' ' }
        subCategory = subCategoryCodestr
        etSubCategory = binding.etSubCategory.text.toString()
            .trim { it <= ' ' }
      }
      updateMeetingSave(meetingApptModel)
    }
  }

  private fun validateForm(): Boolean {
    binding.apply {
      if (spProduct.selectedItem.toString().equals(SELECT_PRODUCT, ignoreCase = true)) {
        tvProductError.text = SELECT_PRODUCT
        tvProductError.requestFocus()
        return false
      } else if (spMeetingDuration.selectedItem
              .toString()
              .equals(SELECT_MEETING_DURATION, ignoreCase = true)
      ) {
        tvMeetingDurationError.visibility = View.VISIBLE
        tvMeetingDurationError.text = SELECT_MEETING_DURATION
        tvMeetingDurationError.requestFocus()
        return false
      } else if (spMeetingObjective.selectedItem
              .toString()
              .equals(SELECT_MEETING_OBJECTIVE, ignoreCase = true)
      ) {
        tvMeetingObjectiveError.visibility = View.VISIBLE
        tvMeetingObjectiveError.text = SELECT_MEETING_OBJECTIVE
        tvMeetingObjectiveError.requestFocus()
        return false
      } else if (spActivityType.selectedItem
              .toString()
              .equals(SELECT_ACTIVITY_TYPE, ignoreCase = true)
      ) {
        tvActivityTypeError.visibility = View.VISIBLE
        tvActivityTypeError.text = SELECT_ACTIVITY_TYPE
        tvActivityTypeError.requestFocus()
        return false
      } else if (spMeetingReason.selectedItem
              .toString()
              .equals(SELECT_MEETING_REASON, ignoreCase = true)
      ) {
        tvMeetingReasonError.visibility = View.VISIBLE
        tvMeetingReasonError.text = SELECT_MEETING_REASON
        tvMeetingReasonError.requestFocus()
        return false
      } else if (etMeetingDescription.text.toString().trim { it <= ' ' }.isEmpty()) {
        tvMeetingDescriptionError.visibility = View.VISIBLE
        tvMeetingDescriptionError.text = MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK
        tvMeetingDescriptionError.requestFocus()
        return false
      } else if (etMeetingDescription.text!!.length <= 10) {
        tvMeetingDescriptionError.visibility = View.VISIBLE
        tvMeetingDescriptionError.text = ATLEAST_TEN_CHARACTERS
        tvMeetingDescriptionError.requestFocus()
        return false
      } else if (llActionables.visibility == View.VISIBLE && spActionables.selectedItem.toString().equals(
              SELECT_ACTIONABLES, ignoreCase = true
          )
      ) {
        tvActionablesError.visibility = View.VISIBLE
        tvActionablesError.text = SELECT_ACTIONABLES
        tvActionablesError.requestFocus()
        return false
      } else if (llActionables.visibility == View.VISIBLE && spCategory.selectedItem.toString().equals(
              SELECT_CATEGORY, ignoreCase = true
          )
      ) {
        tvCategoryError.visibility = View.VISIBLE
        tvCategoryError.text = SELECT_CATEGORY
        tvCategoryError.requestFocus()
        return false
      } else if (llActionables.visibility == View.VISIBLE && etCategory.text!!.length <= 10) {
        tvCategoryCommentsError.visibility = View.VISIBLE
        tvCategoryCommentsError.text = ATLEAST_TEN_CHARACTERS
        tvCategoryCommentsError.requestFocus()
        return false
      } else if (mandatory && llActionables.getVisibility() == View.VISIBLE &&
          spSubCategory.selectedItem.toString().equals(SELECT_SUB_CATEGORY, ignoreCase = true)
      ) {
        tvSubCategoryError.visibility = View.VISIBLE
        tvSubCategoryError.text = SELECT_SUB_CATEGORY
        tvSubCategoryError.requestFocus()
        return false
      } else if (mandatory
          && llActionables.visibility == View.VISIBLE
          && etSubCategory.text!!.length <= 10
      ) {
        tvSubCategoryCommentsError.visibility = View.VISIBLE
        tvSubCategoryCommentsError.text = ATLEAST_TEN_CHARACTERS
        tvSubCategoryCommentsError.requestFocus()
        return false
      }

    }
    return true
  }

  private fun updateMeetingSave(meetingApptModel: MeetingApptModel) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = RequestBody.create("text/plain".toMediaTypeOrNull(), meetingApptModel.json)
      val subscribe = saathiApi.saveUpdateMeeting(request)
          .compose(applyUiModel())
          .subscribe { this.renderUpdateMeetingSave(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderUpdateMeetingSave(uiModel: UiModel<Any>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      alertDialog(
          activity, 0,
          resources.getString(R.string.motilal_oswal),
          UPDATED_MEETING, "OK", true
      ) { action, _ ->
        if (action == MSFDialog.Action.OK) {
          val bundle = Bundle().apply {
            putString(LEAD_ID, argumentData!!.leadId)
            putString(UCID, argumentData!!.ucid)
          }

          navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER.toString(), bundle, true, false)
        }
      }
    }
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
      arrAdapMeetingsObjective.setSpinnerData(
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

  override fun renderScreen(data: UpdateMeetingScreenData) {
    argumentData = data
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

      binding.apply {
        etMeetingDescription.setText(meetingDescription)
        etCustomerComment.setText(customerRemark)
        etCategory.setText(categoryDescription)
        etSubCategory.setText(subCategoryDescription)
        tvDate.text = CRMAppUtil.getDBDate(
            CRMAppUtil.getDBDateObj(
                currentYear, month, day, hour,
                minute
            ), DATE_FORMAT_TO_DISPLAY
        )
      }
      meetingDate = meetingDate!!.replace("T", " ")

    }
  }

  private fun setCountForSpecial(cnt: Int) {
    binding.tvSplAttendeeCount.text = (if (cnt <= 0) "" else String.format("(%s)", cnt.toString()))
  }

  private fun setCount(cnt: Int) {
    binding.tvAttendeeCount.text = (if (cnt <= 0) "" else String.format("(%s)", cnt.toString()))
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
            .subscribe {
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

    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess { data ->
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
      arrAdapSelectProduct.setSpinnerData(binding.spProduct, arrSelectProduct)
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
            .subscribe {
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
      binding.spActivityType.adapter = arrAdapActivityType
      arrAdapActivityType.setSpinnerData(binding.spActivityType, arrActivityType)
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

    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess { data ->
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
      arrAdapMeetingReason.setSpinnerData(binding.spMeetingReason, arrMeetingReason)
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
      binding.spActionables.adapter = arrAdapActionables
      arrAdapActionables.setSpinnerData(binding.spActionables, arrActionables)
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
              actionablesCodeStr = (binding.spActionables.selectedItem as RowData).key
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

    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess { data ->
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
      arrAdapCategory.setSpinnerData(binding.spCategory, arrCategory)
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
            .subscribe {
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

    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess { data ->
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
      if (clientModel.isNoData) {
        holderView.tvAttendeeName.text = clientModel.attendeeName
        holderView.linaerLayout.setOnClickListener(null)
      } else {
        holderView.tvAttendeeName.text = String.format(
            "%s-%s-%s", clientModel.attendeeCode,
            clientModel.attendeeName, clientModel.branchcode
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
            constraint?.let {
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                val request = AutoCompleteAttendeeRequest(
                    URLEncoder.encode(it.toString(), "utf-8"),
                    URLEncoder.encode(it.toString(), "utf-8")
                )
                try {
                  val dataList = saathiApi.getAutoCompleteAttendee(request)
                      .blockingFirst()
                  for (data in dataList) {
                    val employeeName = stringOrEmpty(data.employeeName)
                    val employeeCode = stringOrEmpty(data.employeeCode)
                    val branchCode1 = stringOrEmpty(data.branchCode)
                    if (employeeCode.equals(
                            userManager.getUser()!!.employeeCode, ignoreCase = true
                        )
                    ) {
                      continue
                    }
                    modelList.add(
                        EmployeeModel(
                            employeeName, employeeCode,
                            branchCode1, false
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
          constraint: CharSequence?,
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
          Color.parseColor(
              if (position % 2 == 1) {
                "#F5F5F5"
              } else {
                "#FFFFFF"
              }
          )
      )
    }

    inner class HolderView constructor(parent: View) {
      internal var tvAttendeeName: TextView
      internal var linaerLayout: LinearLayout

      init {
        tvAttendeeName = parent.findViewById(R.id.tvAttendeeName)
        linaerLayout = parent.findViewById(R.id.linaerLayout)
      }
    }
  }

  inner class SplAttendeeAdapter constructor(internal var mContext: Context) : BaseAdapter(),
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
      convertView1: View?,
      parent: ViewGroup
    ): View {
      var convertView = convertView1
      val holderView: HolderView
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
      viewColor(position, convertView)
      if (clientModel.isNoData) {
        holderView.tvAttendeeName.text = clientModel.attendeeName
        holderView.linaerLayout.setOnClickListener(null)
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
        internal val filterResults = FilterResults()

        override fun performFiltering(constraint: CharSequence?): FilterResults {

          val modelList = ArrayList<SplAttendeeModel>()
          try {
            constraint?.let {
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                val request = AutoCompleteAttendeeRequest(
                    URLEncoder.encode(it.toString(), "utf-8"),
                    URLEncoder.encode(it.toString(), "utf-8")
                )
                try {
                  val dataList = saathiApi.getAutoCompleteSplAttendee(request)
                      .blockingFirst()
                  for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, employeeCode1, _, _, _, _, _, _, _, _, _, _, employeeName1, _, branchCode) in dataList) {
                    val employeeName = stringOrEmpty(employeeName1)
                    val employeeCode = stringOrEmpty(employeeCode1)
                    val branchCode1 = stringOrEmpty(branchCode)
                    modelList.add(
                        SplAttendeeModel(
                            employeeName, employeeCode,
                            branchCode1
                        )
                    )
                    filterResults.values = modelList
                    filterResults.count = modelList.size
                  }
                } catch (e: Exception) {
                  modelList.add(
                      SplAttendeeModel.getNoData(getErrorOnFailureNew(e))
                  )
                  filterResults.values = modelList
                  filterResults.count = modelList.size
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
          constraint: CharSequence?,
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
      if (position % 2 == 1) {
        convertView.setBackgroundColor(Color.parseColor("#F5F5F5"))
      } else {
        convertView.setBackgroundColor(Color.parseColor("#FFFFFF"))
      }
    }

    internal inner class HolderView constructor(parent: View) {
      var tvAttendeeName: TextView
      var linaerLayout: LinearLayout

      init {
        tvAttendeeName = parent.findViewById(R.id.tvAttendeeName)
        linaerLayout = parent.findViewById(R.id.linaerLayout)
      }
    }
  }

  inner class AddedAttendeeAdapter constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedAttendeeModel>
  ) :
      BaseAdapter() {
    private val inflater: LayoutInflater

    init {
      inflater = LayoutInflater.from(mContext)
    }

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
      view1: View?,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_attendee, parent, false)

      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = view.findViewById<TextView>(R.id.tvAttendeeName)
      holder.ivDelete = view.findViewById<ImageView>(R.id.ivDelete)

      holder.tvAttendeeName!!.setText(
          String.format(
              "%s-%s", attendeeModel.attendeeEmplyeeCode,
              attendeeModel.attendeeName
          )
      )
      holder.ivDelete!!.setOnClickListener {
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

  inner class AddedSplAttendeeAdapter constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedSplAttendeeModel>
  ) :
      BaseAdapter() {
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
      view1: View?,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false)

      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = view.findViewById<TextView>(R.id.tvAttendeeName)
      holder.ivDelete = view.findViewById<ImageView>(R.id.ivDelete)

      holder.tvAttendeeName!!.setText(
          String.format(
              "%s-%s", attendeeModel.attendeeEmplyeeCode,
              attendeeModel.attendeeName
          )
      )
      holder.ivDelete!!.setOnClickListener {
        addedSplAttendeeCount--
        setCountForSpecial(addedSplAttendeeCount)
        attendeeModelList.removeAt(position)
        notifyDataSetChanged()
      }

      return view
    }

    inner class Holder {
      var tvAttendeeName: TextView? = null
      var ivDelete: ImageView? = null
    }
  }
}