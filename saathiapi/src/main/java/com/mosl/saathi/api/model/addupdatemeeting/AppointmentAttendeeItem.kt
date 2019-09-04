package com.mosl.saathi.api.model.addupdatemeeting

import com.google.gson.annotations.SerializedName

data class AppointmentAttendeeItem(
  @JvmField @SerializedName("AttendeeType") val attendeeType: String,

  @JvmField @SerializedName("AttendeeEmpcode") val attendeeEmpcode: String,

  @JvmField @SerializedName("MeetingAttendeeID") val meetingAttendeeID: String,

  @JvmField @SerializedName("Attended") val attended: Boolean,

  @JvmField @SerializedName("DefaultAttendee") val defaultAttendee: String
)