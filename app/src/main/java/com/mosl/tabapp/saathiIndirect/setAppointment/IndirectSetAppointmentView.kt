package com.mosl.tabapp.saathiIndirect.setAppointment

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent
import com.mosl.tabapp.model.SetAppointmentPostReq
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingUiEvent
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import com.mosl.tabapp.setappointment.SetAppointmentUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectSetAppointmentView :BaseView {

    fun getLeadId(): String
    fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>)
    fun ononActivityCall():Observable<IndirectSetAppointmentUiEvent.onActivityCall>
    fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>)
    fun onActivityTypeSelected(): Observable<Int>
    fun getSelectedActivityType(): LookUp
    fun getSelectedMeetingReason():LookUp
    fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>)
    fun renderDatePicker()
    fun onDatePicker(): Observable<Unit>

    fun onAttendeeClicked(): Observable<Unit>
    fun onAttendeeAddedClicked(): Observable<Unit>
    fun onSplAttendeeAddedClicked(): Observable<Unit>
    fun showAttendeDialog()
    fun getAttendeeSearchText(): Observable<IndirectSetAppointmentUiEvent.SearchTextChange>
    fun onAttendeeItemClickEventFired(): Observable<IndirectSetAppointmentUiEvent.AttendeeItemClick>
    fun toggleSearchState(
        queryEmpty: Boolean,
        isAttendee: Boolean
    )

    fun renderSearchResult(
        uiModel: UiModel<List<Attendee>>,
        isAttendee: Boolean
    )
    fun renderOnAttendeeItemClick(position: Int)

    //spl attendee
    fun onSplAttendeeClicked(): Observable<Unit>
    fun showSplAttendeDialog()
    fun showSplAttendeAddedDialog()
    fun getSplAttendeeSearchText(): Observable<IndirectSetAppointmentUiEvent.SearchTextChangeSplAttendee>
    fun onSplAttendeeItemClickEventFired(): Observable<IndirectSetAppointmentUiEvent.SplAttendeeItemClick>
    fun renderOnSplAttendeeItemClick(position: Int)

    fun onSaveButtonClicked():Observable<Unit>

    fun renderAddUpdates(uiModel: UiModel<SetAppointmentResponseData>)

    fun onResetButtonClicked():Observable<Unit>

    fun onResentButtonRender()

    fun getDate(): String
    fun getSaveData(): SetAppointmentPostReq

    fun getMeetingById(): Observable<IndirectSetAppointmentUiEvent.GetMeeting>

    fun renderMeetingByID(uiModel: UiModel<MeetingByIdItemResponse>)

    fun showAttendeAddedDialog()
}