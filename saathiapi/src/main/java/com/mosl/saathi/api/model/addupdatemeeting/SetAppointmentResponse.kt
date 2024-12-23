package com.mosl.saathi.api.model.addupdatemeeting

import com.google.gson.annotations.SerializedName

data class SetAppointmentResponse(

  @JvmField @SerializedName("repeatEveryWeekCode") val repeatEveryWeekCode: String?,

  @JvmField @SerializedName("repeatEveryDayCode") val repeatEveryDayCode: String?,

  @JvmField @SerializedName("repeatationStartOn") val repeatationStartOn: String?,

  @JvmField @SerializedName("meetingAttendee") val meetingAttendee: List<AppointmentAttendeeItem>?,

  @JvmField @SerializedName("DisplayType") val displayType: String?,

  @JvmField @SerializedName("ActivityTypeID") val activityTypeID: Int?,

  @JvmField @SerializedName("ProductTypeID") val productTypeID: Int?,

  @JvmField @SerializedName("UCID") val uCID: String?,

  @JvmField @SerializedName("repeatationEndsOn") val repeatationEndsOn: String?,

  @JvmField @SerializedName("MeetingDate") val meetingDate: String?,

  @JvmField @SerializedName("MeetingType") val meetingType: String?,

  @JvmField @SerializedName("LeadId") val leadId: String?,

  @JvmField @SerializedName("repeatOnCode") val repeatOnCode: String?
)