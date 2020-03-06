package com.mosl.tabapp.setappointment

import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput

sealed class SetAppointmentUiEvent {
  data class GetClientDetail(val Id: String) : SetAppointmentUiEvent()
  data class ProfileImage(val Id: String) : SetAppointmentUiEvent()
  data class LeadView(val Id: String) : SetAppointmentUiEvent()
  data class GetMeeting(val meetingId: String) : SetAppointmentUiEvent()
  data class Product(val request: LookupRequestInput) : SetAppointmentUiEvent()
  data class ActivityType(val request: LookupRequestInput) : SetAppointmentUiEvent()
  data class Weeks(val request: LookupRequestInput) : SetAppointmentUiEvent()
  data class WeekDays(val request: LookupRequestInput) : SetAppointmentUiEvent()
  data class RepeatMode(val request: LookupRequestInput) : SetAppointmentUiEvent()
  data class SetAppointmentMeeting(val request: String) : SetAppointmentUiEvent()
  /* data class AutoCompleteSplAttendee(val charSequence: CharSequence) : SetAppointmentUiEvent()
   data class AutoCompleteAttendee(val charSequence: CharSequence) : SetAppointmentUiEvent()*/
}