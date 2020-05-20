package com.mosl.tabapp.leadViewDetail.createmeeting

sealed class CreateMeetingUiEvent {
  data class AttendeeItemClick(
    val position: Int
  ) : CreateMeetingUiEvent()
}