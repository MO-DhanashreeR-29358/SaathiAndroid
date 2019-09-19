package com.mosl.tabapp.bankmeeting

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable.Factory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.aubankmeeting.BankListResponse
import com.mosl.saathi.api.model.aubankmeeting.BankRequest
import com.mosl.saathi.api.model.aubankmeeting.BranchListResponse
import com.mosl.saathi.api.model.aubankmeeting.MeetingTypeResponse
import com.mosl.saathi.api.model.aubankmeeting.SubBrokerRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.bankmeeting.adapter.AddedBankEmployeeAdapter
import com.mosl.tabapp.bankmeeting.adapter.BankEmployeeAdapter
import com.mosl.tabapp.bankmeeting.adapter.ClientNameAdapter
import com.mosl.tabapp.bankmeeting.model.AddedBankEmployeeModel
import com.mosl.tabapp.bankmeeting.model.BankMeetingInput
import com.mosl.tabapp.bankmeeting.model.MeetingClientName
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getDateFromString
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentAuBankMeetingBinding
import com.mosl.tabapp.model.AccountDetails
import com.mosl.tabapp.model.bankmeeting.SubBrokerData
import com.mosl.tabapp.model.bankmeeting.SubBrokerData.getNoData
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class BankMeetingFragment : CommonFragment() {
  private lateinit var binding: FragmentAuBankMeetingBinding
  private var arrMeetingType = ArrayList<RowData>()
  private var arrMeetingDuration = ArrayList<RowData>()
  private var arrBank = ArrayList<RowData>()
  private var arrBranch = ArrayList<RowData>()
  private lateinit var meetingTypeAdapter: ArrayAdapter<RowData>
  private lateinit var meetingDurationAdapter: ArrayAdapter<RowData>
  private lateinit var bankAdapter: ArrayAdapter<RowData>
  private lateinit var branchAdapter: ArrayAdapter<RowData>
  private var subBrokerId = -1
  private lateinit var clientNameList: ArrayList<MeetingClientName>
  private lateinit var clientNameAdapter: ClientNameAdapter
  private var addedEmployeeCount: Int = 0
  private lateinit var bankEmployeeAdapter: BankEmployeeAdapter
  private lateinit var addedBankEmployeeAdapter: AddedBankEmployeeAdapter
  private var addedBankEmployeeList = ArrayList<AddedBankEmployeeModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    binding = DataBindingUtil.inflate(
        inflater, layout.fragment_au_bank_meeting, container, false
    )
    setUpView()
    return binding.root
  }

  private fun setUpView() {
    getProfile()
    binding.actvSubBroker.setOnClickListener {
      binding.actvSubBroker.setText("")
      subBrokerId = -1
      if (((binding.spBranch.selectedItem as RowData).value) != "Other")
        searchDialog()
    }

    binding.ibBankEmployee.setOnClickListener {
      if (((binding.spBranch.selectedItem as RowData).value) != "Other")
//        searchDialog("BankEmployee")
        dialogBankEmployee()
    }

    binding.tvBankEmployeeCount.setOnClickListener {
      dialogAddedEmployee()
    }

    clientNameList = ArrayList()
    clientNameAdapter = ClientNameAdapter(clientNameList, requireContext())
    binding.rvClientName.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = clientNameAdapter
    }

    binding.apply {
      llDate.setOnClickListener {
        openDatePicker()
      }

      btnReset.setOnClickListener {
        resetFields()
        resetError()
      }

      btnSave.setOnClickListener {
        submitCalled()
      }

      etClientName.imeOptions = EditorInfo.IME_ACTION_DONE
      etClientName.setOnEditorActionListener { v, actionId, event ->
        val enterText = etClientName.text.toString()
        if (!enterText.isBlank()) {
          if (clientNameList.size < 5) {
            clientNameAdapter.addProduct(
                MeetingClientName(clientName = enterText)
            )
          } else {
            alertDialog(
                requireContext(), 0,
                resources.getString(R.string.motilal_oswal), "Maximum of 5 client can be added",
                "OK", true, null
            )
          }
          etClientName.setText("")
        }
        false
      }
    }
  }

  private val meetingTypeListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(
      parent: AdapterView<*>,
      view: View,
      position: Int,
      id: Long
    ) {
      try {
//        cityCodeStr = (spCity.getSelectedItem() as RowData).key
        binding.tvMeetingTypeError.visibility = View.GONE
        meetingTypeSpinnerChange()
      } catch (ex: Exception) {
        ex.printStackTrace()
      }

    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
  }

  private val bankListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(
      parent: AdapterView<*>,
      view: View,
      position: Int,
      id: Long
    ) {
      try {
        binding.tvBankError.visibility = View.GONE
        callBranchList()
      } catch (ex: Exception) {
        ex.printStackTrace()
      }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
  }

  private val bankBranchListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(
      parent: AdapterView<*>,
      view: View,
      position: Int,
      id: Long
    ) {
      try {
        binding.tvBranchError.visibility = View.GONE
        if (getSelectedBranch().value == "Other") {
          binding.apply {
            etClientName.isEnabled = false
            etClientName.setText("")
            actvSubBroker.setText("")
            subBrokerId = -1
            setCount(0)
            addedBankEmployeeList.clear()
//            actvBankEmployee.setText("")
            resetError()
          }
          clientNameList.clear()
        } else {
          binding.etClientName.isEnabled = true
        }
      } catch (ex: Exception) {
        ex.printStackTrace()
      }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
  }

  private fun openDatePicker() {
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH)
    val d = cal.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

      val dateStr = "$dayOfMonth/${monthOfYear + 1}/$year"
      val pattern = "dd/MM/yyyy"
      val simpleDateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
      val date = simpleDateFormat.parse(dateStr)
      binding.tvDate.text = Factory.getInstance()
          .newEditable(simpleDateFormat.format(date))

    }, y, m, d
    )

    val minDate = Calendar.getInstance()
    minDate.add(Calendar.DATE, -3)
    datePickerDialog.datePicker.maxDate = cal.timeInMillis
    datePickerDialog.datePicker.minDate = minDate.timeInMillis
    datePickerDialog.show()
  }

  private fun searchDialog() {
    activity!!.window
        .setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    val searchList = CustomListPickerDialog(requireContext())
    searchList.threshold = MOSLConstants.SEARCH_LENTH
    searchList.show()
    searchList.setCustomTitle(resources.getString(R.string.str_search))
    val assingViewListner = object : CustomListPickerDialog.ListPickerListneable {
      override fun onListItemSelect(data: Parcelable) {
        val clientModel = data as SubBrokerData
        if (clientModel.isNoData) {
          return
        }
        binding.actvSubBroker.setText(clientModel.subBrokerName)
        subBrokerId = clientModel.bankSubBrokerId
        searchList.dismiss()
      }

      override fun onSearchTextChanged(constraint: CharSequence) {
        searchSubBranch(constraint, searchList)
      }
    }
    searchList.setmListner(assingViewListner)
  }

  private fun dialogBankEmployee() {
    val dialogAttendeeList = Dialog(getContext()!!)
    dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogAttendeeList.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogAttendeeList.setContentView(layout.dialog_attendee_list)
    dialogAttendeeList.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogAttendeeList.setCanceledOnTouchOutside(true)
    val etSearchAttendee =
      dialogAttendeeList.findViewById<DelayAutoCompleteTextView>(R.id.etSearchAttendee)
    etSearchAttendee.imeOptions = EditorInfo.IME_ACTION_DONE
    etSearchAttendee.setOnEditorActionListener { v, actionId, event ->
      hideKeyboard(v)
      false
    }
    etSearchAttendee.threshold = 3
    bankEmployeeAdapter = BankEmployeeAdapter(requireContext(), this)
    etSearchAttendee.setAdapter<BankEmployeeAdapter>(bankEmployeeAdapter)
    etSearchAttendee.setLoadingIndicator(
        dialogAttendeeList.findViewById(R.id.pbLoadingIndicator)
    )
    etSearchAttendee.onItemClickListener =
      AdapterView.OnItemClickListener { parent, view, position, id ->
        val clientModel = bankEmployeeAdapter.getItem(position)
        val has = listHasEmployee(
            addedBankEmployeeList,
//          clientModel.employeeCode
            clientModel.employeeName
        )
        if (!has) {
          if (clientModel.noData) {
            return@OnItemClickListener
          }
          addedEmployeeCount++
          addedBankEmployeeList.add(
              AddedBankEmployeeModel(
                  clientModel.employeeCode,
                  clientModel.employeeName,
                  addedEmployeeCount
              )
          )
          setCount(addedEmployeeCount)
          dialogAttendeeList.dismiss()
        } else {
          Toast.makeText(requireContext(), MOSLConstants.EMPLOYEE_ALREADY_ADDED, Toast.LENGTH_SHORT)
              .show()
          etSearchAttendee.setText("")
        }
      }

    dialogAttendeeList.show()
  }

  private fun dialogAddedEmployee() {
    val dialogAddedAttendee = Dialog(getContext()!!)
    dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogAddedAttendee.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogAddedAttendee.setContentView(layout.dialog_added_bank_employee_list)
    val tvEmpty = dialogAddedAttendee.findViewById<TextView>(R.id.tvEmpty)
    dialogAddedAttendee.window!!
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialogAddedAttendee.setCanceledOnTouchOutside(true)
    val lvAttendee = dialogAddedAttendee.findViewById<View>(R.id.lvAttendee) as ListView
    lvAttendee.emptyView = tvEmpty
    addedBankEmployeeAdapter =
      AddedBankEmployeeAdapter(requireContext(), addedBankEmployeeList, this)
    lvAttendee.adapter = addedBankEmployeeAdapter
    addedBankEmployeeAdapter.notifyDataSetChanged()
    dialogAddedAttendee.show()
  }

  fun setCount(cnt: Int) {
    if (cnt <= 0) {
      binding.tvBankEmployeeCount.text = ""
    } else {
      binding.tvBankEmployeeCount.text = "($cnt)"
    }
    addedEmployeeCount = cnt
  }

  private fun getProfile() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request =
        LookupRequest(AccountDetails.getBusinessUnitId(), MOSLConstants.BANKMEETINGTYPE, null, null)
      val subscribe = saathiApi.getAllLookupMaster(request)
          .compose(applyUiModel())
          .subscribe { this.renderMeetingType(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderMeetingType(uiModel: UiModel<List<MeetingTypeResponse>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      arrMeetingType.clear()
      showOnFailurePopup(error)
    }

    val data = uiModel.data
    if (data != null) {
      arrMeetingType.clear()
      arrMeetingType.add(RowData(0, "-1", "Select"))

      for (i in data.indices) {
        val meetingId = data[i].lookUpid.toString()
        val meetingName = stringOrEmpty(data[i].lookUpName)
        arrMeetingType.add(RowData(i + 1, meetingId, meetingName))
      }
      meetingTypeAdapter =
        ArrayAdapter(requireContext(), layout.simple_spinner_item, arrMeetingType)
      meetingTypeAdapter.setDropDownViewResource(layout.mdropdown)
      binding.spMeetingType.apply {
        adapter = meetingTypeAdapter
        onItemSelectedListener = meetingTypeListener
      }
      meetingDuration()
    }
  }

  private fun meetingDuration() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request =
        LookupRequest(AccountDetails.getBusinessUnitId(), MOSLConstants.MEETINGDURATION, null, null)
      val disposable = saathiApi.getAllLookup(request)
          .compose(applyUiModel())
          .subscribe { this.renderMeetingDuration(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderMeetingDuration(uiModel: UiModel<List<LookupItem>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      arrMeetingDuration.clear()
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrMeetingDuration.clear()
      arrMeetingDuration.add(RowData(0, "-1", MOSLConstants.SELECT_MEETING_DURATION))
      for (i in data.indices) {
        val (_, _, _, _, _, lookUpid, lookUpName1) = data[i]
        val lookUpId = stringOrEmpty(lookUpid.toString())
        val lookUpName = stringOrEmpty(lookUpName1)
        arrMeetingDuration.add(
            RowData(i + 1, lookUpId, lookUpName)
        )
      }
      meetingDurationAdapter = ArrayAdapter(
          requireContext(),
          layout.simple_spinner_item, arrMeetingDuration
      )
      meetingDurationAdapter.setDropDownViewResource(layout.mdropdown)
      binding.apply {
        spMeetingDuration.adapter = meetingDurationAdapter
      }
      meetingDurationAdapter.notifyDataSetChanged()
    }
  }

  private fun callBankList() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request = BankRequest()
      val subscribe = saathiApi.getBankList(request)
          .compose(applyUiModel())
          .subscribe { this.renderBank(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderBank(uiModel: UiModel<List<BankListResponse>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      arrBank.clear()
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrBank.clear()
      arrBank.add(RowData(0, "-1", MOSLConstants.SELECT_BANK))
      for (i in data.indices) {
        val (bankName, bankId, _, _, _, _) = data[i]
        val id = stringOrEmpty(bankId.toString())
        val name = stringOrEmpty(bankName)
        arrBank.add(RowData(i + 1, id, name))
      }
      bankAdapter = ArrayAdapter(
          requireContext(),
          layout.simple_spinner_item, arrBank
      )
      bankAdapter.setDropDownViewResource(layout.mdropdown)
      binding.spBank.apply {
        adapter = bankAdapter
        onItemSelectedListener = bankListener
      }
    }
  }

  private fun callBranchList() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val request =
        BankRequest(
            bankId = 1
        )
      val subscribe = saathiApi.getBankBranchList(request)
          .compose(applyUiModel())
          .subscribe { this.renderBranch(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderBranch(uiModel: UiModel<List<BranchListResponse>>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      arrBranch.clear()
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      arrBranch.clear()
      arrBranch.add(RowData(0, "-1", MOSLConstants.SELECT_BRANCH))
      for (i in data.indices) {
        val (branchName, branchId, _, _, _, _, _) = data[i]
        val id = stringOrEmpty(branchId.toString())
        val name = stringOrEmpty(branchName)
        arrBranch.add(RowData(i + 1, id, name))
      }
      branchAdapter = ArrayAdapter(
          requireContext(),
          layout.simple_spinner_item, arrBranch
      )
      branchAdapter.setDropDownViewResource(layout.mdropdown)
      binding.spBranch.apply {
        adapter = branchAdapter
        onItemSelectedListener = bankBranchListener
      }
    }
  }

  private fun meetingTypeSpinnerChange() {
    binding.apply {
      clientNameList.clear()
      when (spMeetingType.getMeetingType()) {
        PERSONAL_MEETING -> {
          groupBankBranch.toggelVisibility(true)
          ilSubject.toggelInputLayoutVisibility(false)
          ilClientName.toggelInputLayoutVisibility(false)
          rvClientName.visibility = View.GONE
          callBankList()
        }
        TRAINING -> {
          groupBankBranch.toggelVisibility(true)
          ilSubject.toggelInputLayoutVisibility(true)
          ilClientName.toggelInputLayoutVisibility(false)
          rvClientName.visibility = View.GONE
          callBankList()
        }
        JOINT_MEETING -> {
          groupBankBranch.toggelVisibility(true)
          ilSubject.toggelInputLayoutVisibility(false)
          ilClientName.toggelInputLayoutVisibility(true)
          rvClientName.visibility = View.VISIBLE
          callBankList()
        }
        else -> {
          groupBankBranch.toggelVisibility(false)
          ilSubject.toggelInputLayoutVisibility(false)
          ilClientName.toggelInputLayoutVisibility(false)
          rvClientName.visibility = View.GONE
        }
      }
    }
  }

  private fun searchSubBranch(
    constraint: CharSequence?,
    searchList: CustomListPickerDialog
  ) {
    if (constraint == null) return
    val profileRequest: SubBrokerRequest
    try {
      profileRequest = SubBrokerRequest(
          URLEncoder.encode("1", "utf-8"),
          URLEncoder.encode("1"/*getSelectedBranch().value*/, "utf-8"),
          URLEncoder.encode(constraint.toString(), "utf-8")
      )
    } catch (e: UnsupportedEncodingException) {
      return
    }

    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val disposable = saathiApi.getSubBroker(profileRequest)
          .compose(applyUiModel())
          .subscribe { uiModel ->
            if (uiModel.inProgress) {
              hideKeyboard(binding.actvSubBroker)
              searchList.showProgress(View.VISIBLE)
            } else {
              searchList.showProgress(View.GONE)
            }

            val error = uiModel.error
            if (error != null) {
              val modelList = ArrayList<SubBrokerData>()
              modelList.add(getNoData(getErrorOnFailureNew(error)))
              val myAssineeAdapter = SearchAdapter(requireContext(), modelList)
              searchList.setAdapter(myAssineeAdapter)
            }

            val data = uiModel.data
            if (data != null) {
              val modelList = ArrayList<SubBrokerData>()
              for (datum in data) {
                modelList.add(
                    SubBrokerData(
                        stringOrEmpty(datum.subBrokerName),
                        datum.bankSubBrokerId,
                        datum.bankBranchId,
                        datum.bankId,
                        false
                    )
                )
              }
              if (modelList.isEmpty()) {
                modelList.add(getNoData(MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE))
              }
              val myAssineeAdapter = SearchAdapter(requireContext(), modelList)
              searchList.setAdapter(myAssineeAdapter)
            }
          }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun getMeetingDate(): String {
    return getDateFromString(
        binding.tvDate.text.toString(),
        "dd/MM/yyyy",
        "dd-MMM-yyyy"
    )
  }

  private fun getMeetingType(): RowData {
    return binding.spMeetingType.selectedItem as RowData
  }

  private fun getMeetingDuration(): RowData {
    return binding.spMeetingDuration.selectedItem as RowData
  }

  private fun getMeetingDescription(): String {
    return binding.etMeetingDescription.text.toString()
  }

  private fun getSelectedBank(): RowData {
    return binding.spBank.selectedItem as RowData
  }

  private fun getSelectedBranch(): RowData {
    return binding.spBranch.selectedItem as RowData
  }

  private fun getSubBroker(): Int {
    return subBrokerId
  }

  private fun getSubject(): String {
    return binding.etSubject.text.toString()
  }

  private fun getBankEmployee(): String {
    var employeeId = ""
    for (a in addedBankEmployeeList) {
      employeeId += if (employeeId.isBlank())
        a.employeeCode
      else
        "," + a.employeeCode
    }
    return employeeId
  }

  private fun submitCalled() {
    resetError()
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      if (getFormInput().validateForm(binding)) {
        val subscribe = saathiApi.inserBankMeeting(transformInput(getFormInput()))
            .compose(applyUiModel())
            .subscribe { this.render(it) }
      }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun render(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      arrBranch.clear()
      showOnFailurePopup(error)
    }
    val data = uiModel.data
    if (data != null) {
      alertDialog(
          requireContext(), 0,
          resources.getString(R.string.motilal_oswal), "Meeting Completed Successfully",
          "OK", true, null
      )
      resetFields()
    }
  }

  private fun resetFields() {
    binding.apply {
      etMeetingDescription.setText("")
      tvDate.text = ""
      spMeetingType.setSelection(0)
      spMeetingDuration.setSelection(0)
      spBank.setSelection(0)
      spBranch.setSelection(0)
      actvSubBroker.setText("")
      subBrokerId = -1
//      actvBankEmployee.setText("")
      setCount(0)
      etSubject.setText("")
      etClientName.setText("")
      clientNameList.clear()
      addedBankEmployeeList.clear()
    }
  }

  private fun resetError() {
    binding.apply {
      tvDateError.toggleTextView(false)
      tvMeetingTypeError.toggleTextView(false)
      tvMeetingDurationError.toggleTextView(false)
      ilMeetingDescription.setErrorMessages()
      tvBankError.toggleTextView(false)
      tvBranchError.toggleTextView(false)
      ilSubBroker.setErrorMessages()
//      ilBankEmployee.setErrorMessages()
      tvBankEmployeeError.toggleTextView(false)
      ilSubject.setErrorMessages()
    }
  }

  private fun getFormInput(): BankMeetingInput {
    var clientNameValue = ""
    for (a in clientNameAdapter.listProduct) {
      clientNameValue += if (clientNameValue.isBlank())
        a.clientName
      else
        "," + a.clientName
    }
    binding.apply {
      return BankMeetingInput(
          meetingDate = getMeetingDate(),
          meetingTypeId = getMeetingType().key,
          meetingDurationId = getMeetingDuration().key,
          meetingDescription = getMeetingDescription(),
          bankId = if (spBank.isSpinnerVisible()) getSelectedBank().key else "-1",
          bankBranchId = if (spBranch.isSpinnerVisible()) getSelectedBranch().key else "-1",
          disabledFields = if (spBranch.isSpinnerVisible()) getSelectedBranch().value == "Other" else false,
          subBrokerId = getSubBroker(),
          bankEmployeeCode = getBankEmployee(),
          subject = getSubject(),
          isSubjectVisible = ((binding.spMeetingType.selectedItem as RowData).value == "Training"),
          isClientNameVisible = ((binding.spMeetingType.selectedItem as RowData).value == "Joint Meeting"),
          clientName = clientNameValue
      )
    }
  }

  companion object {
    const val PERSONAL_MEETING = 1
    const val TRAINING = 2
    const val JOINT_MEETING = 3
    const val PersonalMeeting = "Personal Meeting"
    const val TrainingValue = "Training"
    const val JointMeeting = "Joint Meeting"

    fun listHasEmployee(
      addedAttendeeModelList: List<AddedBankEmployeeModel>?,
      empID: String
    ): Boolean {
      //    addedAttendeeModelList
      var has = false
      if (addedAttendeeModelList != null) {
        for (atten in addedAttendeeModelList) {
          if (atten.employeeCode.trim { it <= ' ' }.equals(
                  empID.trim { it <= ' ' },
                  ignoreCase = true
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
}


