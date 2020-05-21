package com.mosl.tabapp.leadViewDetail.createmeeting

sealed class CreateMeetingUiEvent {
  data class AttendeeItemClick(val position: Int) : CreateMeetingUiEvent()
  data class SplAttendeeItemClick(val position: Int) : CreateMeetingUiEvent()

  data class SearchTextChange(val text: String) : CreateMeetingUiEvent()
  data class SearchTextChangeSplAttendee(val text: String) : CreateMeetingUiEvent()
}