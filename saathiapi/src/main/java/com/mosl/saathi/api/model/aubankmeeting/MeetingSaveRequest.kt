package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class MeetingSaveRequest(
  @JvmField @SerializedName("BankMeetingId") val bankMeetingId: String,
  @JvmField @SerializedName("MeetingTypeId") val meetingTypeId: String,
  @JvmField @SerializedName("MeetingDate") val meetingDate: String,
  @JvmField @SerializedName("MeetingDurationId") val meetingDurationId: String,
  @JvmField @SerializedName("MeetingDescription") val meetingDescription: String,
  @JvmField @SerializedName("BankId") val bankId: String,
  @JvmField @SerializedName("BankBranchId") val bankBranchId: String,
  @JvmField @SerializedName("SubBrokerId") val subBrokerId: String,
  @JvmField @SerializedName("Subject") val subject: String,
  @JvmField @SerializedName("NoOfClient") val noOfClient: String,
  @JvmField @SerializedName("ClientName") val clientName: String
)