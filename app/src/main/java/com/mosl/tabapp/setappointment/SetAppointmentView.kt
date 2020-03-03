package com.mosl.tabapp.setappointment

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadFieldValueDataResponse
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse

import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.GetClientDetail
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.LeadView
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.ProfileImage
import io.reactivex.Observable

interface SetAppointmentView : BaseView {

  fun getClientDetail(): Observable<GetClientDetail>
  fun getProfileImage(): Observable<ProfileImage>
  fun getLeadViews(): Observable<LeadView>

  fun renderClientDetail(uiModel: UiModel<ClientDetailIResponseData>)
  fun renderProfileImage(uiModel: UiModel<String>)
  fun renderLeadViews(uiModel: UiModel<LeadViewItemResponse>)

}