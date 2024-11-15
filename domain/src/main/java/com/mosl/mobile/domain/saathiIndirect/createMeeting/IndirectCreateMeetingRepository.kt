package com.mosl.mobile.domain.saathiIndirect.createMeeting

import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import io.reactivex.rxjava3.core.Observable

interface IndirectCreateMeetingRepository {

    fun getMeetingReason(): Observable<List<LookUp>>
    fun getMeetingObjective(): Observable<List<LookUp>>
    fun getLocationAddress(): Observable<List<LookUp>>
    fun getTravellingMode():Observable<List<LookUp>>
    fun getInteresedIn():Observable<List<LookUp>>
    fun getAttendeeData(searchText: String): Observable<List<Attendee>>

}