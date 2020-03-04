package com.mosl.mobile.domain.setappointment

import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import io.reactivex.Observable

interface SetAppointmentRepository {
  fun getMeetingById(meetingId: String): Observable<MeetingByIdItemResponse>
  fun getClientDetail(uCID: String): Observable<ClientDetailIResponseData>
  fun getClientImage(id: String): Observable<String>
  fun getLeadViewById(id: String): Observable<LeadViewItemResponse>
  fun getAllLookUp(request: LookupRequestInput): Observable<List<LookupItemResponse>>
  fun setAppointmentMeeting()
  fun getAutoCompleteSplAttendee(request: AutoCompleteAttendeeRequestInput)
  fun getAutoCompleteAttendee(request: AutoCompleteAttendeeRequestInput)

}