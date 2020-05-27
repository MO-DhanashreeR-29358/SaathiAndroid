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