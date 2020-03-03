package com.mosl.mobile.domain.setappointment

import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import io.reactivex.Observable

interface SetAppointmentRepository {
  fun getMeetingById(id: String): Observable<MeetingByIdItemData>
  fun getClientDetail(uCID: String): Observable<ClientDetailIResponseData>
  fun getClientImage(id: String): Observable<String>
  fun getLeadViewById(id: String): Observable<LeadViewItemResponse>
  fun getAllLookUp(request: LookupRequestInput)
  fun setAppointmentMeeting()
  fun getAutoCompleteSplAttendee(request: AutoCompleteAttendeeRequestInput)
  fun getAutoCompleteAttendee(request: AutoCompleteAttendeeRequestInput)

}