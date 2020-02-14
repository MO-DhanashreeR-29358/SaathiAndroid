package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.leadviewdetail.activities.Meeting

sealed class LeadViewActivitiesUiEvent {
  data class AppointmentEvent(val data: Appointment) : LeadViewActivitiesUiEvent()
  data class MeetingEvent(val data: Meeting) : LeadViewActivitiesUiEvent()
}