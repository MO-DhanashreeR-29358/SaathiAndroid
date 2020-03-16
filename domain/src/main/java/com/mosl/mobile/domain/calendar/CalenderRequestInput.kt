package com.mosl.mobile.domain.calendar

data class CalenderRequestInput(
  @JvmField val meetingDate: String,
  @JvmField val meetingType: String,
  @JvmField val attendeeCode: String
)