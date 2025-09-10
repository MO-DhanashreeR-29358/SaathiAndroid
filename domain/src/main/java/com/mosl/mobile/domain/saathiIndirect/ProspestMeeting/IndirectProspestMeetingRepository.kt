package com.mosl.mobile.domain.saathiIndirect.ProspestMeeting


import io.reactivex.rxjava3.core.Observable

interface IndirectProspestMeetingRepository {

    fun fnSearchProspectMeetingClient(strClientCode:String?, strFranchieeCode:String?, strLeadId:String?): Observable<List<ClientSearchModel>>

    fun fnSearchProspectMeetingFRCode(strClientCode:String?, strFranchieeCode:String?, strLeadId:String?): Observable<List<ClientSearchModel>>

}