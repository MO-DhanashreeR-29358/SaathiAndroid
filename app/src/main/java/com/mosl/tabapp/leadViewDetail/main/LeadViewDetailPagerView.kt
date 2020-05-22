package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail

interface LeadViewDetailPagerView : BaseView {

  fun renderLeadDetail(uiModel: UiModel<LeadDetail>)

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UC_ID = "arg_uc_id"
  }
}