package com.mosl.mobile.domain.leadviewdetail.updatemeeting

import io.reactivex.Observable

interface UpdateMeetingRepository {
  fun getMeeting(id: String): Observable<Meeting>

  fun getMeetingObjective(): Observable<List<Lookup>>
}