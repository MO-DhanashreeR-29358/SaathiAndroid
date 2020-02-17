package com.mosl.tabapp.leadViewDetail

import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.leadviewdetail.activities.Meeting

interface LeadViewActivitiesNavigator {
  fun openLeadViewActivitiesDetailScreen(data: Meeting)
  fun openSetAppointmentScreen(data: Appointment)
  fun openUpdateMeetingScreen(data: Appointment)
}