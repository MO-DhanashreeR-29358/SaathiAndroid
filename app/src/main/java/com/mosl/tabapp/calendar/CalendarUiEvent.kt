package com.mosl.tabapp.calendar

import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.LeadActivitiesAppointData

sealed class CalendarUiEvent {
  data class ItemClick(@JvmField val data: CalenderItemResponse) :
    CalendarUiEvent()

  data class CallAPi(@JvmField val selectionDate: String) : CalendarUiEvent()
}