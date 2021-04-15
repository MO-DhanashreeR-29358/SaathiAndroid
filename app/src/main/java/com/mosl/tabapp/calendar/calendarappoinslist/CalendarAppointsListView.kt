package com.mosl.tabapp.calendar.calendarappoinslist

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.tabapp.calendar.CalendarUiEvent.CallAPi
import io.reactivex.rxjava3.core.Observable

interface CalendarAppointsListView : BaseView {
  fun onDatePicker(): Observable<Unit>
  fun callCalendarApi(): Observable<CallAPi>
  fun renderDatePicker()
  fun renderCalendar(uiModel: UiModel<List<CalenderItemResponse>>)
}