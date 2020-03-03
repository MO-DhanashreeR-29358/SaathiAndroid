package com.mosl.tabapp.setappointment

sealed class SetAppointmentUiEvent {
  data class GetClientDetail(val Id: String) : SetAppointmentUiEvent()
  data class ProfileImage(val Id: String) : SetAppointmentUiEvent()
  data class LeadView(val Id: String) : SetAppointmentUiEvent()
}