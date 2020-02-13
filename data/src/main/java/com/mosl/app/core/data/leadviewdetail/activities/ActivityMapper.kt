package com.mosl.app.core.data.leadviewdetail.activities

import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.leadviewdetail.activities.Meeting
import com.mosl.saathi.api.model.leadviewactivities.AppointmentRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentResponse
import com.mosl.saathi.api.model.leadviewactivities.Filter
import com.mosl.saathi.api.model.leadviewactivities.MeetingRequest
import com.mosl.saathi.api.model.leadviewactivities.MeetingResponse
import com.mosl.saathi.api.model.leadviewactivities.Request

private const val REPORT_PAGE_SIZE = "50"
private const val APPOINTMENT_PAGE = "1"
private const val MEETING_PAGE = "0"
private const val CALCULATED_PAGE = "0"
private const val TOTAL_COUNT = "0"

fun ActivityRequest.mapToAppointmentRequest(): AppointmentRequest {
  return AppointmentRequest(
      request = Request(
          leadID = leadId,
          pageSize = REPORT_PAGE_SIZE,
          page = APPOINTMENT_PAGE,
          calculatedPage = CALCULATED_PAGE,
          totalCount = TOTAL_COUNT,
          uCID = ucid
      ),
      filter = Filter(null, null, null, null, null, null, null),
      isAdminMode = true
  )
}

fun ActivityRequest.mapToMeetingRequest(): MeetingRequest {
  return MeetingRequest(
      request = Request(
          leadID = leadId,
          pageSize = REPORT_PAGE_SIZE,
          page = MEETING_PAGE,
          calculatedPage = CALCULATED_PAGE,
          totalCount = TOTAL_COUNT,
          uCID = ucid
      ),
      filter = Filter(null, null, null, null, null, null, null),
      isAdminMode = true
  )
}

fun List<AppointmentResponse>.mapToListOfAppointment(): List<Appointment> {

}

fun List<MeetingResponse>.mapToListOfMeeting(): List<Meeting> {

}