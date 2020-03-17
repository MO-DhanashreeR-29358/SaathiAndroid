package com.mosl.tabapp.calendar

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.LeadActivitiesAppointData
import com.mosl.tabapp.calendar.CalendarUiEvent.ItemClick
import io.reactivex.Observable
import java.util.Date

interface CalendarFragmentView : BaseView {
  fun getSelectedDatesString(): String
  fun getSelectedDate(): Date?
  fun getResponseList(): List<CalenderItemResponse>
  fun onItemClicked(): Observable<ItemClick>
  fun renderCalendar(uiModel: UiModel<List<CalenderItemResponse>>)

}