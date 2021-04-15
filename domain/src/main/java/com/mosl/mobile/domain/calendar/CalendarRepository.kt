package com.mosl.mobile.domain.calendar

import io.reactivex.rxjava3.core.Observable

interface CalendarRepository {
  fun postCalenderByDate(requestInput: CalenderRequestInput) :Observable<List<CalenderItemResponse>>
}