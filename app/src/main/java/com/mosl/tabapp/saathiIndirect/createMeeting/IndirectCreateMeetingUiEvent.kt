package com.mosl.tabapp.saathiIndirect.createMeeting

sealed class IndirectCreateMeetingUiEvent {

    object OnMeetingDurationCall:IndirectCreateMeetingUiEvent()
    object  OnMeetingObjectiveCall:IndirectCreateMeetingUiEvent()
    object OnLocationAddressCall:IndirectCreateMeetingUiEvent()
    object OnTravellingModeCall:IndirectCreateMeetingUiEvent()

}