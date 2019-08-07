package com.mosl.tabapp.bankmeeting

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mosl.app.core.data.UiModel
import com.mosl.app.core.data.applyUiModel
import com.mosl.saathi.api.model.aubankmeeting.BankRequest
import com.mosl.saathi.api.model.aubankmeeting.BranchListResponse
import com.mosl.saathi.api.model.aubankmeeting.MeetingTypeResponse
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.FragmentAuBankMeetingBinding
import com.mosl.tabapp.model.AccountDetails
import com.mosl.tabapp.ui.MOSLCommonFragment
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
        .compose(applyUiModel<List<MeetingTypeResponse>>())
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
        .compose(applyUiModel<String>())
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
        .compose(applyUiModel<List<BranchListResponse>>())
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

  companion object {
    const val PERSONAL_MEETING = 1
    const val TRAINING = 2
    const val JOINT_MEETING = 3
    const val PersonalMeeting = "Personal Meeting"
    const val TrainingValue = "Training"
    const val JointMeeting = "Joint Meeting"
  }
}


