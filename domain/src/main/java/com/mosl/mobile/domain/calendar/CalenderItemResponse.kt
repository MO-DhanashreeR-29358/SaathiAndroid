package com.mosl.mobile.domain.calendar

data class CalenderItemResponse(
  @JvmField val name: String,
  @JvmField val meetingDate: String,
  @JvmField val leadId: String,
  @JvmField val dateDisplay: String

)