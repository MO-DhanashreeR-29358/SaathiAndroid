package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetail
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadDetailMainView : BaseView {
  fun getLeadId(): String
  fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetail>)

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UC_ID = "arg_uc_id"
  }
}