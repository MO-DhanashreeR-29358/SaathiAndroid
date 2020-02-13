package com.mosl.app.core.data.leadviewdetail.activities

import com.mosl.app.core.data.util.safeMap
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
  return safeMap {
    it.mapToAppointment()
  }

}

fun List<MeetingResponse>.mapToListOfMeeting(): List<Meeting> {
  return safeMap {
    it.mapToMeeting()
  }
}

private fun AppointmentResponse.mapToAppointment(): Appointment {
  return Appointment(
      mobileNumber = mOBNO,
      product = product,
      email = eMAIL,
      name = name,
      attendeeEmployee = attendeeEmp,
      meetingId = meetingID,
      status = status,
      createdBy = createdBy,
      activityType = activityType,
      time = tIME,
      statusName = statusName,
      date = dATE,
      leadId = leadID,
      repeatOnCode = repeatOnCode,
      ucid = ucid
  )
}

private fun MeetingResponse.mapToMeeting(): Meeting {
  return Meeting(
      mobileNumber = mOBNO,
      category = category,
      actionable = actionable,
      product = product,
      email = eMAIL,
      name = name,
      attendeeEmployee = attendeeEmp,
      travellingMode = travellingMode,
      meetingAddress = meetingAddress,
      meetingId = meetingID,
      stage = stage,
      meetingAddressType = meetingAddressType,
      customerServiceRemark = customerServiceRemark,
      meetingReason = meetingReason,
      status = status,
      queryDescription = queryDesc,
      comment = comments,
      amount = amount,
      activityType = activityType,
      time = tIME,
      statusName = statusName,
      date = dATE,
      leadId = leadID,
      meetingDuration = meetingDuration,
      pincode = pincode,
      location = location
  )
}