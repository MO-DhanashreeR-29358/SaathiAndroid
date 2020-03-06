package com.mosl.mobile.domain.addupdatemeeting

data class SetAppointmentResponseData(

  @JvmField val repeatEveryWeekCode: String,

  @JvmField val repeatEveryDayCode: String,

  @JvmField val repeatationStartOn: String,

  @JvmField val meetingAttendee: List<AppointmentAttendeeItemResponse>,

  @JvmField val displayType: String?,

  @JvmField val activityTypeID: Int,

  @JvmField val productTypeID: Int,

  @JvmField val uCID: String,

  @JvmField val repeatationEndsOn: String,

  @JvmField val meetingDate: String,

  @JvmField val meetingType: String,

  @JvmField val leadId: String,

  @JvmField val repeatOnCode: String
)