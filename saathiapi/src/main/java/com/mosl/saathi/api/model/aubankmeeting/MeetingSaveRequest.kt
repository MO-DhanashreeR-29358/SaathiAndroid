package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class MeetingSaveRequest(
//  @JvmField @SerializedName("BankMeetingId") val bankMeetingId: String,
  @JvmField @SerializedName("MeetingTypeId") val meetingTypeId: String,
  @JvmField @SerializedName("MeetingDate") val meetingDate: String,
  @JvmField @SerializedName("MeetingDurationId") val meetingDurationId: Int,
  @JvmField @SerializedName("MeetingDescription") val meetingDescription: String,
  @JvmField @SerializedName("BankId") val bankId: Int,
  @JvmField @SerializedName("BankBranchId") val bankBranchId: Int,
  @JvmField @SerializedName("SubBrokerId") val subBrokerId: Int,
  @JvmField @SerializedName("Subject") val subject: String,
  @JvmField @SerializedName("NoOfClient") val noOfClient: Int,
  @JvmField @SerializedName("ClientName") val clientName: String,
  @JvmField @SerializedName("BankEmployeeCode") val bankEmployeeCode: String
)