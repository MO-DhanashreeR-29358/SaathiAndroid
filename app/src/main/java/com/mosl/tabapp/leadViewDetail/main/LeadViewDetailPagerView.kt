package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import io.reactivex.rxjava3.core.Observable

interface LeadViewDetailPagerView : BaseView {

  fun renderLeadDetail(uiModel: UiModel<LeadDetail>)
  fun renderClientDetailAndImage(uiModel: UiModel<ClientDetailAndImage>)

  fun onThreeVerticalDotsClicked(): Observable<Unit>
  fun showLeadViewDialog()

  fun onShareLinkClicked(): Observable<LeadViewDetailUiEvent.ShareLinkClick>
  fun renderOnShareLinkClick(leadId: String)
  fun onSuggestMeClicked(): Observable<LeadViewDetailUiEvent.SuggestMeClick>
  fun renderOnSuggestMeClick(uiModel: UiModel<String>)
  fun onCopyLinkClicked(): Observable<LeadViewDetailUiEvent.CopyLinkClick>
  fun renderOnCopyLinkClick()
  fun onOaoFormClicked(): Observable<LeadViewDetailUiEvent.OaoFormClick>
  fun onOpenAccountClicked(): Observable<LeadViewDetailUiEvent.OpenAccountClick>
  fun renderOpenAccountClick()
  fun onCreateMeetingClicked(): Observable<LeadViewDetailUiEvent.CreateMeetingClick>
  fun renderCreateMeetingClick(id: Int)
  fun onSetAppointmentClick(): Observable<LeadViewDetailUiEvent.SetAppointmentClick>
  fun onCallClicked(): Observable<LeadViewDetailUiEvent.CallClick>
  fun renderCallClick()
  fun getUcid(): String
  fun getLeadId(): String
  fun onSMSClicked(): Observable<LeadViewDetailUiEvent.SMSClick>
  fun onRetryClick(): Observable<LeadViewDetailUiEvent.RetryClick>
  fun renderSMSClick()
  fun onUpdateCreateMeetingId(): Observable<LeadViewDetailUiEvent.UpdateCreateMeetingId>
  fun renderUpdateCreateMeetingId(id: Int)

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UC_ID = "arg_uc_id"
  }
}