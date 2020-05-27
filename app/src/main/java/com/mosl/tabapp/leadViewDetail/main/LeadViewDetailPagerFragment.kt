package com.mosl.tabapp.leadViewDetail.main

import android.os.Bundle
import android.util.Base64
import android.view.View
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadviewMainBinding
import javax.inject.Inject

class LeadViewDetailPagerFragment : CommonFragment(), LeadViewDetailPagerView {

  private lateinit var binding: FragLeadviewMainBinding
  @Inject internal lateinit var presenter: LeadViewDetailPagerPresenter

  private var mobile: String? = null
  private var oaoUserId: String? = null
  private var diyCode: String? = null
  private var diyLink: String? = null
  private var createMeetingId: String? = null
  private var interestedIn: String? = null
  private var ucId: String? = null
  private var email: String? = null
  private var leadOwner: String? = null
  private var sourceName: String? = null
  private var prospectSourceName: String? = null
  private var prospectSourceDetail: String? = null

  override fun getLayout(): Int {
    return R.layout.frag_leadview_main
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadviewMainBinding.bind(view)
    presenter.start()
  }

  override fun renderLeadDetail(uiModel: UiModel<LeadDetail>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      binding.apply {
        createMeetingId = it.createMeetingId
        leadOwner = it.leadOwner
        sourceName = it.source
        prospectSourceName = it.prospectSource
        prospectSourceDetail = it.prospectSourceDetail
        oaoUserId = it.oaoUserId
        diyCode = it.diyCode
        diyLink = it.diyLink
        it.leadProducts
            .forEach {
              interestedIn = it.interestedIn
              if (it.isDefault) {
                includeClient.detail.apply {
                  tvClientCode.text = it.clientCode
                  tvCurrentStage.text = it.stageName
                  tvCurrentStageSince.text = "${it.pendingDays} Days"
                }
              }
            }
        ucId = it.personalDetail.ucId
        email = it.personalDetail.email
        mobile = it.personalDetail.mobile
        includeClient.detail.tvName.text = it.personalDetail.name
        hideAppTitle()
        includeClient.detail.tvLeadId.text = it.id
        includeClient.detail.tvBranchCode.text = it.branchCode
      }
    }
  }

  override fun renderClientDetailAndImage(uiModel: UiModel<ClientDetailAndImage>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      binding.includeClient.detail.apply {
        tvName.text = it.clientDetail.name.toUpperCase()
        hideAppTitle()
        tvLeadId.text = it.clientDetail.id
        tvCurrentStage.text = it.clientDetail.currentStage
        tvClientCode.text = it.clientDetail.clientCode
        tvBranchCode.text = it.clientDetail.branchCode
        mobile = it.clientDetail.mobile
        oaoUserId = it.clientDetail.oaoUserId
        val byte = Base64.decode(it.image, Base64.DEFAULT)
        CRMAppUtil.setImage(
            when {
              byte != null -> byte
              else -> byteArrayOf()
            },
            ivLead, requireContext()

        )
      }
    }
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }

}