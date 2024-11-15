package com.mosl.tabapp.saathiIndirect.createMeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectCreateMeetingView :BaseView{

    fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>)
    fun onActivityTypeSelected(): Observable<Int>
    fun getSelectedActivityType(): LookUp
    fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>)
    fun onMeetingDurationCall():Observable<IndirectCreateMeetingUiEvent.OnMeetingDurationCall>
    fun fnRenderMeetingDuration(uiModel: UiModel<List<LookUp>>)
    fun onLocationAddressCall():Observable<IndirectCreateMeetingUiEvent.OnLocationAddressCall>
    fun fnRenderLocationAddress(uiModel: UiModel<List<LookUp>>)
    fun onMeetingObjectiveCall():Observable<IndirectCreateMeetingUiEvent.OnMeetingObjectiveCall>
    fun fnRenderMeetingObjective(uiModel: UiModel<List<LookUp>>)
    fun onTravellingModeCall():Observable<IndirectCreateMeetingUiEvent.OnTravellingModeCall>
    fun fnRenderTravellingMode(uiModel: UiModel<List<LookUp>>)
    fun onIntrestedInCall():Observable<IndirectCreateMeetingUiEvent.OnIntrestedInCall>
    fun fnRenderIntrestedIn(uiModel: UiModel<List<LookUp>>)
    fun renderDatePicker()
    fun onDatePicker(): Observable<Unit>
    fun onAttendeeClicked(): Observable<Unit>
    fun onAttendeeAddedClicked(): Observable<Unit>
    fun showAttendeDialog()
    fun getAttendeeSearchText(): Observable<IndirectCreateMeetingUiEvent.SearchTextChange>
    fun onAttendeeItemClickEventFired(): Observable<IndirectCreateMeetingUiEvent.AttendeeItemClick>
    fun toggleSearchState(
        queryEmpty: Boolean,
        isAttendee: Boolean
    )

    fun renderSearchResult(
        uiModel: UiModel<List<Attendee>>,
        isAttendee: Boolean
    )
    fun renderOnAttendeeItemClick(position: Int)
}