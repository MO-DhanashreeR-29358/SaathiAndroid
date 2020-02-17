package com.mosl.mobile.domain.leadviewdetail.activities

import io.reactivex.Observable

interface LeadViewDetailActivitiesRepository {
  fun getActivityData(request: ActivityRequest): Observable<ActivityData>
  fun deleteAppointment(
    meetingId: String,
    url: String
  ): Observable<String>
}