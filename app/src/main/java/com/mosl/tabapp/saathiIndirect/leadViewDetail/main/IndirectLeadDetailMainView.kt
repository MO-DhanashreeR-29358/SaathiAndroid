package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailUiEvent
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadDetailMainView : BaseView {
  fun getLeadId(): String

  fun getUcid(): String
  fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>)

  fun onThreeVerticalDotsClicked(): Observable<Unit>
  fun showLeadViewDialog()

  fun onCreateMeetingClicked(): Observable<IndirectLeadViewDetailUiEvent.CreateMeetingClick>
  fun renderCreateMeetingClick(id: Int)
  fun onSetAppointmentClick(): Observable<IndirectLeadViewDetailUiEvent.SetAppointmentClick>
  fun onCallClicked(): Observable<IndirectLeadViewDetailUiEvent.CallClick>
  fun renderCallClick()

  fun onSMSClicked(): Observable<IndirectLeadViewDetailUiEvent.SMSClick>
  fun onRetryClick(): Observable<IndirectLeadViewDetailUiEvent.RetryClick>

  fun renderSMSClick()

  companion object {
    const val ARG_LEAD_ID = "arg_lead_id"
    const val ARG_UC_ID = "arg_uc_id"
  }
}