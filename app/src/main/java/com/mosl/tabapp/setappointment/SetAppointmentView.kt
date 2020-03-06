package com.mosl.tabapp.setappointment

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.ActivityType

import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.GetClientDetail
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.GetMeeting
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.LeadView
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.Product
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.ProfileImage
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.RepeatMode
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.SetAppointmentMeeting
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.WeekDays
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent.Weeks
import io.reactivex.Observable

interface SetAppointmentView : BaseView {

  fun getClientDetailData(): Observable<GetClientDetail>
  fun getProfileImage(): Observable<ProfileImage>
  fun getLeadViews(): Observable<LeadView>
  fun getMeetingById(): Observable<GetMeeting>
  fun getProduct(): Observable<Product>
  fun getActivityType(): Observable<ActivityType>
  fun getWeeks(): Observable<Weeks>
  fun getWeekDays(): Observable<WeekDays>
  fun getRepeatMode(): Observable<RepeatMode>
  fun saveAppointmentMeeting(): Observable<SetAppointmentMeeting>
  /*fun getAutoCompleteSplAttendee():Observable<AutoCompleteSplAttendee>
  fun getAutoCompleteAttendee():Observable<AutoCompleteAttendee>*/

  fun renderClientDetail(uiModel: UiModel<ClientDetailIResponseData>)
  fun renderProfileImage(uiModel: UiModel<String>)
  fun renderLeadView(uiModel: UiModel<LeadViewItemResponse>)
  fun renderMeetingByID(uiModel: UiModel<MeetingByIdItemResponse>)

  fun renderProduct(uiModel: UiModel<List<LookupItemResponse>>)
  fun renderActivityType(uiModel: UiModel<List<LookupItemResponse>>)
  fun renderWeeks(uiModel: UiModel<List<LookupItemResponse>>)
  fun renderWeekDays(uiModel: UiModel<List<LookupItemResponse>>)
  fun renderRepeatMode(uiModel: UiModel<List<LookupItemResponse>>)
  fun renderAddUpdates(uiModel: UiModel<SetAppointmentResponseData>)
  /* fun renderAutoCompleteSplAttendee(uiModel: UiModel<List<AutoCompleteAttendeeItemResponse>>)
   fun renderAutoCompleteAttendee(uiModel: UiModel<List<AutoCompleteAttendeeItemResponse>>)*/

}