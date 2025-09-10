package com.mosl.tabapp.model

import com.google.gson.annotations.SerializedName

data class SplMeetingAttendeeItem(
    @SerializedName("attended")
    var attended: Boolean? = null,

    @SerializedName("attendeeName")
    var attendeeName: String? = null,

    @SerializedName("meetingAttendeeCode")
    var meetingAttendeeCode: String? = null,

    @SerializedName("attendeeEmplyeeCode")
    var attendeeEmplyeeCode: String? = null,

    @SerializedName("attendeeType")
    var attendeeType: String? = null
)
