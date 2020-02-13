package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName

data class AppointmentResponse(

  @JvmField @SerializedName("MOBNO") val mOBNO: String,

  @JvmField @SerializedName("Product") val product: String,

  @JvmField @SerializedName("EMAIL") val eMAIL: String,

  @JvmField @SerializedName("Name") val name: String,

  @JvmField @SerializedName("AttendeeEmp") val attendeeEmp: String,

  @JvmField @SerializedName("MeetingID") val meetingID: String,

  @JvmField @SerializedName("Stage") val stage: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("DATE_DISPLAY") val dATEDISPLAY: String,

  @JvmField @SerializedName("Status") val status: String,

  @JvmField @SerializedName("CreatedBy") val createdBy: String,

  @JvmField @SerializedName("ActivityType") val activityType: String,

  @JvmField @SerializedName("TIME") val tIME: String,

  @JvmField @SerializedName("StatusName") val statusName: String,

  @JvmField @SerializedName("ATTENDEE_4") val aTTENDEE4: Any?,

  @JvmField @SerializedName("ATTENDEE_3") val aTTENDEE3: String?,

  @JvmField @SerializedName("ATTENDEE_2") val aTTENDEE2: String?,

  @JvmField @SerializedName("ATTENDEE_1") val aTTENDEE1: String?,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean?,

  @JvmField @SerializedName("DATE") val dATE: String,

  @JvmField @SerializedName("ATTENDEE_5") val aTTENDEE5: Any?,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any?,

  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("repeatOnCode") val repeatOnCode: String = "",

  @JvmField @SerializedName("ShowAppointmentUpdate") val showAppointmentUpdate: Boolean,

  @JvmField @SerializedName("UCID") val ucid: String
)