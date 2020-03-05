package com.mosl.mobile.domain.addupdatemeeting

data class AppointmentAttendeeItemResponse(
  @JvmField val attendeeType: String,

  @JvmField val attendeeEmpcode: String,

  @JvmField val meetingAttendeeID: String,

  @JvmField val attended: Boolean,

  @JvmField val defaultAttendee: String
)