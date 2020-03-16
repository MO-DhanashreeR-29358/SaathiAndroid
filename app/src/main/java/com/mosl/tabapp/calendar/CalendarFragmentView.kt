package com.mosl.tabapp.calendar

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import java.util.Date

interface CalendarFragmentView : BaseView {
  fun getSelectedDatesString(): String
  fun getSelectedDate(): Date?

  fun renderCalendar(uiModel: UiModel<List<CalenderItemResponse>>)
}