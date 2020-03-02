package com.mosl.mobile.domain.setappointment

import io.reactivex.Observable

interface SetAppointmentRepository {
  fun getMeetingById(request: MeetingByIdInput): Observable<MeetingByIdItemData>
  fun getClientDetail(request: ClientDetailRequestInput)
  fun getClientImage(request: ClientDetailRequestInput)
  fun getLeadViewByid(request: LeadViewRequestInput)
  fun getAllLookUp(request: LookupRequestInput)
  fun setAppointmentMeeting()
  fun getAutoCompleteSplAttendee(request: AutoCompleteAttendeeRequestInput)
  fun getAutoCompleteAttendee(request: AutoCompleteAttendeeRequestInput)



}