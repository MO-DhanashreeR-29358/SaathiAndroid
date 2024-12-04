package com.mosl.tabapp.saathiIndirect.prospestMeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import io.reactivex.rxjava3.core.Observable

interface IndirectProspestMeetingView:BaseView {
    fun onTextSearchCalled():Observable<IndirectProspestMeetingUiEvent.SearchTextChange>
    fun onTextSearchedRender(uiModel: UiModel<List<ClientSearchModel>>)
    fun onFCodeTextSearchCalled():Observable<IndirectProspestMeetingUiEvent.SearchFCodeTextChange>
    fun onFCodeTextSearchRender(uiModel: UiModel<List<ClientSearchModel>>)
    fun onResetButtonClicked():Observable<Unit>
    fun onSearchButtonClicked():Observable<Unit>
    fun onResetButtonRender()



    // create meeting copied

    fun getLeadId(): String
    fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>)
    fun ononActivityCall():Observable<IndirectProspestMeetingUiEvent.onActivityCall>
    fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>)
    fun onActivityTypeSelected(): Observable<Int>
    fun getSelectedActivityType(): LookUp
    fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>)
    fun onMeetingDurationCall():Observable<IndirectProspestMeetingUiEvent.OnMeetingDurationCall>
    fun fnRenderMeetingDuration(uiModel: UiModel<List<LookUp>>)
    fun onLocationAddressCall():Observable<IndirectProspestMeetingUiEvent.OnLocationAddressCall>
    fun fnRenderLocationAddress(uiModel: UiModel<List<LookUp>>)
    fun onMeetingObjectiveCall():Observable<IndirectProspestMeetingUiEvent.OnMeetingObjectiveCall>
    fun fnRenderMeetingObjective(uiModel: UiModel<List<LookUp>>)
    fun onTravellingModeCall():Observable<IndirectProspestMeetingUiEvent.OnTravellingModeCall>
    fun fnRenderTravellingMode(uiModel: UiModel<List<LookUp>>)
    fun onIntrestedInCall():Observable<IndirectProspestMeetingUiEvent.OnIntrestedInCall>
    fun fnRenderIntrestedIn(uiModel: UiModel<List<LookUp>>)

    fun fnGetSelectedIntrestedIn():LookUp?
    fun renderDatePicker()
    fun onDatePicker(): Observable<Unit>
    fun onAttendeeClicked(): Observable<Unit>
    fun onAttendeeAddedClicked(): Observable<Unit>
    fun showAttendeDialog()
    fun getAttendeeSearchText(): Observable<IndirectProspestMeetingUiEvent.SearchTextChangeCreateMeeting>
    fun onAttendeeItemClickEventFired(): Observable<IndirectProspestMeetingUiEvent.AttendeeItemClick>
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
    fun getInputData(): String

    fun onResetClicked(): Observable<Unit>
    fun onSaveClicked(): Observable<Unit>

}