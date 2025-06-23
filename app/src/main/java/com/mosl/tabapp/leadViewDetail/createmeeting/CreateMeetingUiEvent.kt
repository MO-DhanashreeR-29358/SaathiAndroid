package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.tabapp.geoLocation.LocationItem

sealed class CreateMeetingUiEvent {
  data class AttendeeItemClick(val position: Int) : CreateMeetingUiEvent()
  data class SplAttendeeItemClick(val position: Int) : CreateMeetingUiEvent()

  data class SearchTextChange(val text: String) : CreateMeetingUiEvent()
  data class SearchTextChangeSplAttendee(val text: String) : CreateMeetingUiEvent()
  object SendOtpRequest : CreateMeetingUiEvent()
  data class ValidationStatus(val boolean: Boolean) : CreateMeetingUiEvent()
  data class SaveRequestData(val data: String) : CreateMeetingUiEvent()
  object SaveConfirmClick : CreateMeetingUiEvent()

  object TouchedUntouchedRequest : CreateMeetingUiEvent()
  object FeedbackRequest : CreateMeetingUiEvent()
  data class OnLocationSaveClick(val data: LocationItem) : CreateMeetingUiEvent()

  object CallChipsApi : CreateMeetingUiEvent()

  object CallActivityTypeApi : CreateMeetingUiEvent()
  object CallMeetingDurationApi : CreateMeetingUiEvent()
  object CallInterestedInApi : CreateMeetingUiEvent()
  object CallMeetingObjectiveApi : CreateMeetingUiEvent()
  object CallTravellingModeApi : CreateMeetingUiEvent()
  object CallMeetingAddressApi : CreateMeetingUiEvent()

 }