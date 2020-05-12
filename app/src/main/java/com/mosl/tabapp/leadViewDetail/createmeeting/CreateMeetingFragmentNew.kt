package com.mosl.tabapp.leadViewDetail.createmeeting

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragCreateMeetingBinding
import com.mosl.tabapp.util.SpinnerAdapterFactory
import java.util.Date
import javax.inject.Inject

class CreateMeetingFragmentNew : CommonFragment(), CreateMeetingView {
  lateinit var binding: FragCreateMeetingBinding
  @Inject
  lateinit var presenter: CreateMeetingPresenter
  lateinit var productAdapter: ArrayAdapter<LookUp>
  lateinit var activityAdapter: ArrayAdapter<LookUp>
  lateinit var meetingObjectiveAdapter: ArrayAdapter<LookUp>
  lateinit var meetingDurationAdapter: ArrayAdapter<LookUp>
  lateinit var meetingLocationAdapter: ArrayAdapter<LookUp>
  lateinit var travellingModeAdapter: ArrayAdapter<LookUp>

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
            String.format("%s Days", clientDetail.currentStage)
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

  companion object {
    const val ARG_LEAD_ID = "LeadID"
    const val ARG_UCID = "UCID"
  }
}