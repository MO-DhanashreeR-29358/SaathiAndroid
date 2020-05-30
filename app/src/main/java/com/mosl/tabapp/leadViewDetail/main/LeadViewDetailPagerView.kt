package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import io.reactivex.Observable

interface LeadViewDetailPagerView : BaseView {

  fun renderLeadDetail(uiModel: UiModel<LeadDetail>)
  fun renderClientDetailAndImage(uiModel: UiModel<ClientDetailAndImage>)

  fun onThreeVerticalDotsClicked(): Observable<Unit>
  fun showLeadViewDialog()

  fun onShareLinkClicked(): Observable<ShareLinkClick>
  fun renderOnShareLinkClick(leadId: String)
  fun onSuggestMeClicked(): Observable<SuggestMeClick>
  fun renderOnSuggestMeClick(uiModel: UiModel<String>)

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UC_ID = "arg_uc_id"
  }
}