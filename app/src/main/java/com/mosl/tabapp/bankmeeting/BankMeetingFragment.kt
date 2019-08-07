package com.mosl.tabapp.bankmeeting

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mosl.app.core.data.UiModel
import com.mosl.app.core.data.applyUiModel
import com.mosl.saathi.api.model.aubankmeeting.BankRequest
import com.mosl.saathi.api.model.aubankmeeting.BranchListResponse
import com.mosl.saathi.api.model.aubankmeeting.MeetingTypeResponse
import com.mosl.saathi.api.model.aubankmeeting.SubBrokerRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.FragmentAuBankMeetingBinding
import com.mosl.tabapp.model.AccountDetails
import com.mosl.tabapp.model.bankmeeting.SubBrokerData
import com.mosl.tabapp.model.bankmeeting.SubBrokerData.getNoData
import com.mosl.tabapp.ui.MOSLCommonFragment
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.ArrayList

class BankMeetingFragment : MOSLCommonFragment() {
  private lateinit var binding: FragmentAuBankMeetingBinding
  private var arrMeetingType = ArrayList<RowData>()
  private var arrMeetingDuration = ArrayList<RowData>()
  private var arrBank = ArrayList<RowData>()
  private var arrBranch = ArrayList<RowData>()
  private lateinit var meetingTypeAdapter: ArrayAdapter<RowData>
  private lateinit var meetingDurationAdapter: ArrayAdapter<RowData>
  private lateinit var bankAdapter: ArrayAdapter<RowData>
  private lateinit var branchAdapter: ArrayAdapter<RowData>

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
      activity!!.window
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
      val searchList = CustomListPickerDialog(mainActivity)
      searchList.threshold = MOSLConstants.SEARCH_LENTH
      searchList.show()
      searchList.setCustomTitle(resources.getString(R.string.str_assing))
      val assingViewListner = object : CustomListPickerDialog.ListPickerListneable {
        override fun onListItemSelect(data: Parcelable) {
          val clientModel = data as SubBrokerData
          if (clientModel.isNoData) {
            return
          }
          binding.actvSubBroker.setText(clientModel.subBrokerName)
          searchList.dismiss()
        }

        override fun onSearchTextChanged(constraint: CharSequence) {
          searchUserBusinessProfile(constraint, searchList)
        }
      }
      searchList.setmListner(assingViewListner)
    }
  }

  private val meetingTypeListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
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
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
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

  private fun getProfile() {
    if (CRMAppUtil.isNetworkEnabled(mainActivity)) {
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
      meetingTypeAdapter = ArrayAdapter(mainActivity, layout.simple_spinner_item, arrMeetingType)
      meetingTypeAdapter.setDropDownViewResource(layout.mdropdown)
      binding.apply {
        spMeetingType.apply {
          adapter = meetingTypeAdapter
          onItemSelectedListener = meetingTypeListener
        }
      }
      meetingDuration()
    }
  }

  private fun meetingDuration() {
    if (CRMAppUtil.isNetworkEnabled(mainActivity)) {
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
        mainActivity,
        layout.simple_spinner_item, arrMeetingDuration
      )
      meetingDurationAdapter.setDropDownViewResource(layout.mdropdown)
      binding.apply {
        spMeetingDuration.adapter = meetingDurationAdapter
//      spMeetingDuration.setOnItemSelectedListener(selectMeetingDurationListener)
      }
      meetingDurationAdapter.notifyDataSetChanged()
    }
  }

  private fun callBankList() {
    if (CRMAppUtil.isNetworkEnabled(mainActivity)) {
      val request =
        BankRequest(
          null,
          null,
          null,
          null,
          null,
          null,
          1
        )
      val subscribe = saathiApi.getBankList(request)
        .compose(applyUiModel())
        .subscribe { this.renderBank(it) }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderBank(it: UiModel<String>) {
    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
  }

  private fun callBranchList() {
    if (CRMAppUtil.isNetworkEnabled(mainActivity)) {
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
        mainActivity,
        layout.simple_spinner_item, arrBranch
      )
      branchAdapter.setDropDownViewResource(layout.mdropdown)
      binding.apply {
        spBranch.adapter = branchAdapter
//      spMeetingDuration.setOnItemSelectedListener(selectMeetingDurationListener)
      }
      branchAdapter.notifyDataSetChanged()
    }
  }

  private fun meetingTypeSpinnerChange() {
    binding.apply {
      when (spMeetingType.getMeetingType()) {
        PERSONAL_MEETING -> {
          groupBankBranch.toggelVisibility(true)
          callBranchList()
        }
        TRAINING -> {
          groupBankBranch.toggelVisibility(true)
          ilSubject.toggelInputLayoutVisibility(true)
          callBankList()
        }
        JOINT_MEETING -> {
          groupBankBranch.toggelVisibility(true)
          ilSubject.toggelInputLayoutVisibility(false)
          callBankList()
        }
        else -> {
          groupBankBranch.toggelVisibility(false)
          ilSubject.toggelInputLayoutVisibility(false)
        }
      }
    }
  }

  internal fun searchUserBusinessProfile(
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

    if (CRMAppUtil.isNetworkEnabled(mainActivity)) {
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
//            val modelList = ArrayList<EmployeeModel>()
//            modelList.add(EmployeeModel.getNoData(getErrorOnFailure(error)))
//            val myAssineeAdapter = AttendeeAdapter(mainActivity, modelList)
//            searchList.setAdapter(myAssineeAdapter)
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
            val myAssineeAdapter = SearchAdapter(mainActivity, modelList)
            searchList.setAdapter(myAssineeAdapter)
          }
        }
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun getSelectedBank(): RowData {
    return binding.spBank.selectedItem as RowData
  }

  private fun getSelectedBranch(): RowData {
    return binding.spBranch.selectedItem as RowData
  }

  companion object {
    const val PERSONAL_MEETING = 1
    const val TRAINING = 2
    const val JOINT_MEETING = 3
    const val PersonalMeeting = "Personal Meeting"
    const val TrainingValue = "Training"
    const val JointMeeting = "Joint Meeting"
  }
}


