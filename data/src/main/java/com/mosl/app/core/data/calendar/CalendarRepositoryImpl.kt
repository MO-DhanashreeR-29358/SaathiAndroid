package com.mosl.app.core.data.calendar

import com.mosl.mobile.domain.calendar.CalendarRepository
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : CalendarRepository {
  override fun postCalenderByDate(requestInput: CalenderRequestInput): Observable<List<CalenderItemResponse>> {
    val calendarRequest = requestInput.mapCalendarRequest()
    return saathiApi.postCalenderByDate(calendarRequest).map {
        it?.calendarItemResponseTransform()
    }
  }
}