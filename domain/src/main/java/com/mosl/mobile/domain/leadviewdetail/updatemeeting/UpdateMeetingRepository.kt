package com.mosl.mobile.domain.leadviewdetail.updatemeeting

import io.reactivex.rxjava3.core.Observable

interface UpdateMeetingRepository {
  fun getMeeting(id: String): Observable<Meeting>

  fun getMeetingObjective(): Observable<List<Lookup>>
}