package com.mosl.tabapp.calendar

import com.mosl.app.core.data.calendar.CalendarRepositoryImpl
import com.mosl.mobile.domain.calendar.CalendarRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CalendarModule {
  @Binds
  abstract fun bindsCalendarFragmentView(fragment: CalendarFragmentNew): CalendarFragmentView

  @Binds
  abstract fun bindsCalendarRepository(repository: CalendarRepositoryImpl): CalendarRepository
}