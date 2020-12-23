package com.mosl.mobile.domain.createmeeting

import io.reactivex.Observable

interface CreateMeetingRepository {
  fun getClientDetail(ucid: Int): Observable<ClientDetail>
  fun getClientImage(ucid: Int): Observable<String>
  fun getLeadView(leadId: String): Observable<ClientDetail>
  fun getLookup(
    type: Int,
    boolean: Boolean? = null,
    parentId: String? = null
  ): Observable<List<LookUp>>

  fun getInitialPageData(createMeetingData: CreateMeetingData): Observable<ClientData>
  fun getAddress(
    meetingID: String,
    leadId: String
  ): Observable<String>

  fun getCustomerServiceData(): Observable<Pair<List<LookUp>, List<LookUp>>>
  fun getFranchiseIFAUserData(): Observable<Pair<List<LookUp>, List<LookUp>>>
  fun getAttendeeData(searchText: String): Observable<List<Attendee>>
  fun getSplAttendeeData(searchText: String): Observable<List<Attendee>>
  fun sendOTP(data: OTPRequestData): Observable<String>
  fun saveRequest(data: String): Observable<AddUpdateMeeting>
}