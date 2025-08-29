package com.mosl.tabapp.saathiIndirect.prospestMeeting


sealed class IndirectProspestMeetingUiEvent {
    data class SearchTextChange(val text: String) :IndirectProspestMeetingUiEvent()

    data class SearchFCodeTextChange(val text: String) :IndirectProspestMeetingUiEvent()
    
    // create meeting

    object onActivityCall: IndirectProspestMeetingUiEvent()
    object OnMeetingDurationCall: IndirectProspestMeetingUiEvent()
    object  OnMeetingObjectiveCall: IndirectProspestMeetingUiEvent()
    object OnLocationAddressCall: IndirectProspestMeetingUiEvent()
    object OnTravellingModeCall: IndirectProspestMeetingUiEvent()
    object OnIntrestedInCall: IndirectProspestMeetingUiEvent()
    data class SearchTextChangeCreateMeeting(val text: String) : IndirectProspestMeetingUiEvent()
    data class AttendeeItemClick(val position: Int): IndirectProspestMeetingUiEvent()

    object OnSaveCall: IndirectProspestMeetingUiEvent()

    object OnBDMSaveCall: IndirectProspestMeetingUiEvent()
}