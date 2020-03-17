package com.mosl.tabapp.calendar
import com.mosl.tabapp.calendar.calendarappoinslist.CalendarAppointsListFragmentNew
import com.mosl.tabapp.calendar.calendarappoinslist.CalendarAppointsListView
import dagger.Binds
import dagger.Module

@Module
abstract class CalendarModule {
  @Binds
  abstract fun bindsCalendarFragmentView(fragment: CalendarFragmentNew): CalendarFragmentView

  @Binds
  abstract fun bindsCalendarAppointsListFragmentView(fragment: CalendarAppointsListFragmentNew): CalendarAppointsListView
}