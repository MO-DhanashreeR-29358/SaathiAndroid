package com.mosl.tabapp.saathiIndirect.createMeeting

import com.mosl.tabapp.geoLocation.LocationItem

sealed class IndirectCreateMeetingUiEvent {

    object onActivityCall:IndirectCreateMeetingUiEvent()
    object OnMeetingDurationCall:IndirectCreateMeetingUiEvent()
    object  OnMeetingObjectiveCall:IndirectCreateMeetingUiEvent()
    object OnLocationAddressCall:IndirectCreateMeetingUiEvent()
    object OnTravellingModeCall:IndirectCreateMeetingUiEvent()
    object OnIntrestedInCall:IndirectCreateMeetingUiEvent()
    data class SearchTextChange(val text: String) :IndirectCreateMeetingUiEvent()
    data class AttendeeItemClick(val position: Int):IndirectCreateMeetingUiEvent()
    data class OnIndirectLocationSaveClick(val data: LocationItem) : IndirectCreateMeetingUiEvent()
}