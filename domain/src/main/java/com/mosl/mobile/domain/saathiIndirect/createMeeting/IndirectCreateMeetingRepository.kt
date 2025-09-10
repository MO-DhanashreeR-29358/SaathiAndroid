package com.mosl.mobile.domain.saathiIndirect.createMeeting

import com.mosl.mobile.domain.createmeeting.AddUpdateMeeting
import com.mosl.mobile.domain.createmeeting.AddUpdateMeetingIndirect
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingNewResposne
import io.reactivex.rxjava3.core.Observable

interface IndirectCreateMeetingRepository {

    fun getMeetingReason(): Observable<List<LookUp>>
    fun getMeetingObjective(): Observable<List<LookUp>>
    fun getLocationAddress(): Observable<List<LookUp>>
    fun getTravellingMode():Observable<List<LookUp>>
    fun getInteresedIn(interestedInId : Int):Observable<List<LookUp>>
    fun getAttendeeData(searchText: String): Observable<List<Attendee>>

    fun getAddress(
        meetingID: String,
        leadId: String
    ): Observable<String>

    fun saveRequest(data: String): Observable<AddUpdateMeetingIndirect>

    fun saveBDMRequest(data: String): Observable<AddUpdateMeetingIndirect>
    fun saveRequetProspectMeeting(data: String): Observable<AddUpdateMeetingNewResposne>

    fun saveRequetBDMProspectMeeting(data: String): Observable<AddUpdateMeetingNewResposne>
}