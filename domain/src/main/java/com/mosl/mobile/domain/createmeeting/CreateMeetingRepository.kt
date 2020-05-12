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
}