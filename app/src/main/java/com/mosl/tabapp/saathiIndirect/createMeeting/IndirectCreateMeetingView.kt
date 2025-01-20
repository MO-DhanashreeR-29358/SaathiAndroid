package com.mosl.tabapp.saathiIndirect.createMeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.AddUpdateMeeting
import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.tabapp.geoLocation.LocationItem
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import io.reactivex.rxjava3.core.Observable

import com.mosl.tabapp.model.CreateMeetingPostReq
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentUiEvent

interface IndirectCreateMeetingView :BaseView{

    fun getLeadId(): String
    fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>)
    fun ononActivityCall():Observable<IndirectCreateMeetingUiEvent.onActivityCall>
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

    fun fnGetSelectedIntrestedIn():LookUp?
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

    fun getAttendeeList(): List<AddedAttendee>
    fun getPincode(): String?
    fun getAmount(): String?
    fun getMeetingAddress(): String?
    fun getDate(): String
    fun getSelectedMeetingDuration(): LookUp?
    fun getSelectedMeetingObjective(): LookUp?
    fun getSelectedMeetingReason(): LookUp
    fun getMeetingDesc(): String

    fun onTravellingModeSelected(): Observable<Int>
    fun getSelectedTravellingMode(): LookUp
    fun renderOnTravellingModeClick(lookUp: LookUp)
    fun getTravellingmodeList(): List<CreateMeetingTravellingModel>

    fun getMeetingLatitude(): String?
    fun getMeetingLongitude(): String?
    fun onMeetingLocationSelected(): Observable<Int>
    fun getSelectedMeetingLocation(): LookUp
    fun renderAddress(uiModel: UiModel<String>)
    fun getInputData(): CreateMeetingPostReq

    fun onResetClicked(): Observable<Unit>

    fun renderOnResetClicked()
    fun onSaveClicked(): Observable<Unit>
    fun renderSaveRequest(uiModel: UiModel<AddUpdateMeeting>)

    fun onIndirectLocationSaveEventFired() : Observable<IndirectCreateMeetingUiEvent.OnIndirectLocationSaveClick>

    fun renderLocationSave(locationItem: LocationItem)

    fun showAttendeAddedDialog()

    fun getMeetingById(): Observable<IndirectCreateMeetingUiEvent.GetMeeting>

    fun renderMeetingByID(uiModel: UiModel<MeetingByIdItemResponse>)
    fun toggleOTPVisibility(show: Boolean)

}