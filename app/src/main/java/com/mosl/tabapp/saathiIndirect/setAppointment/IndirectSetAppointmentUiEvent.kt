package com.mosl.tabapp.saathiIndirect.setAppointment

import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingUiEvent
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent
import io.reactivex.rxjava3.core.Observable

sealed class IndirectSetAppointmentUiEvent{

//data class onActivityTypeSelected(val objLookup:LookUp):IndirectSetAppointmentUiEvent()
object onActivityCall: IndirectSetAppointmentUiEvent()
    data class SearchTextChange(val text: String) : IndirectSetAppointmentUiEvent()
    data class AttendeeItemClick(val position: Int): IndirectSetAppointmentUiEvent()

    data class SearchTextChangeSplAttendee(val text: String) :IndirectSetAppointmentUiEvent()

    data class SplAttendeeItemClick(val position: Int) :IndirectSetAppointmentUiEvent()

    data class GetMeeting(val meetingId: String) : IndirectSetAppointmentUiEvent()
}
