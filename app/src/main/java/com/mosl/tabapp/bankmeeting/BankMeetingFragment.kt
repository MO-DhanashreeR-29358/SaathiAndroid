package com.mosl.tabapp.bankmeeting

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mosl.app.core.data.UiModel
import com.mosl.app.core.data.applyUiModel
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
  private lateinit var meetingTypeAdapter: ArrayAdapter<RowData>
  private lateinit var meetingDurationAdapter: ArrayAdapter<RowData>

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
//    val subscribe = saathiApi.getBankList()
//      .compose<UiModel<String>>(applyUiModel<String>())
//      .subscribe { this.renderBank(it) }
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
        val cityId = data[i].lookUpid.toString()
        val cityName = stringOrEmpty(data[i].lookUpName)
        arrMeetingType.add(RowData(i + 1, cityId, cityName))
      }
      meetingTypeAdapter = ArrayAdapter(mainActivity, layout.simple_spinner_item, arrMeetingType)
      meetingTypeAdapter.setDropDownViewResource(layout.mdropdown)
      binding.apply {
        spMeetingType.adapter = meetingTypeAdapter
//      spCity.setOnItemSelectedListener(cityListener)
      }
      meetingDuration()
    }
  }

  private fun renderBank(it: UiModel<String>) {
    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
  }
}


