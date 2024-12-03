package com.mosl.tabapp.saathiIndirect.prospestMeeting

sealed class IndirectProspestMeetingUiEvent {
    data class SearchTextChange(val text: String) :IndirectProspestMeetingUiEvent()

    data class SearchFCodeTextChange(val text: String) :IndirectProspestMeetingUiEvent()
}