package com.mosl.saathi.api.model.calender

import com.google.gson.annotations.SerializedName

data class CalenderRequest(

  @JvmField @SerializedName("MeetingDate") val meetingDate: String,

  @JvmField @SerializedName("MeetingType") val meetingType: String,

  @JvmField @SerializedName("AttendeeCode") val attendeeCode: String
)