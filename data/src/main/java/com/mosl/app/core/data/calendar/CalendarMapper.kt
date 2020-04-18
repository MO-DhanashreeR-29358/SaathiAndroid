package com.mosl.app.core.data.calendar

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.saathi.api.model.calender.CalenderItem
import com.mosl.saathi.api.model.calender.CalenderRequest

fun CalenderRequestInput.mapCalendarRequest(): CalenderRequest {
  return CalenderRequest(
    meetingDate = meetingDate,
    attendeeCode = attendeeCode,
    meetingType = meetingType
  )
}

fun List<CalenderItem>.calendarItemResponseTransform(): List<CalenderItemResponse> {
  return safeMap {
    CalenderItemResponse(
      name = it.name ?:"",
      meetingDate = it.meetingDate ?: "",
      leadId = it.leadId ?: "",
      dateDisplay = it.dateDisplay ?: ""
    )
  }
}

