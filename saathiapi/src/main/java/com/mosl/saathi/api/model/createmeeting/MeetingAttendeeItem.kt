package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class MeetingAttendeeItem(

  @JvmField @SerializedName("AttendeeType") val attendeeType: String?,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("MeetingAttendeeID") val meetingAttendeeID: String?,

  @JvmField @SerializedName("AttendeeEmpcode") val attendeeEmpcode: String?,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("MeetingID") val meetingID: Int,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("AttendeeEmpName") val attendeeEmpName: String?,

  @JvmField @SerializedName("Attended") val attended: Boolean,

  @JvmField @SerializedName("DefaultAttendee") val defaultAttendee: String?
)