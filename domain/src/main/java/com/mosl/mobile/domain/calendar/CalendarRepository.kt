package com.mosl.mobile.domain.calendar

import io.reactivex.Observable

interface CalendarRepository {
  fun postCalenderByDate(requestInput: CalenderRequestInput) :Observable<List<CalenderItemResponse>>
}