package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import com.google.gson.annotations.SerializedName

data class registerRequest (
    @SerializedName("data" ) var data : registerData? = registerData()
)
data class registerData (
    @SerializedName("campaignId" ) var campaignId : String?         = null,
    @SerializedName("data"       ) var data       : ArrayList<MeetingFeedbackData> = arrayListOf()
)
data class MeetingFeedbackData (
    @SerializedName("Role"                ) var Role                : String? = null,
    @SerializedName("Region"              ) var Region              : String? = null,
    @SerializedName("LeadId"              ) var LeadId              : String? = null,
    @SerializedName("RHName"              ) var RHName              : String? = null,
    @SerializedName("RMName"              ) var RMName              : String? = null,
    @SerializedName("Clientcode"          ) var Clientcode          : String? = null,
    @SerializedName("RMEmpcode"           ) var RMEmpcode           : String? = null,
    @SerializedName("MeetingDate"         ) var MeetingDate         : String? = null,
    @SerializedName("MeetingTime"         ) var MeetingTime         : String? = null,
    @SerializedName("ActivityType"        ) var ActivityType        : String? = null,
    @SerializedName("CustomerName"        ) var CustomerName        : String? = null,
    @SerializedName("AttendeeDetails"     ) var AttendeeDetails     : String? = null,
    @SerializedName("LeadCreationDate"    ) var LeadCreationDate    : String? = null,
    @SerializedName("RMSupervisorName"    ) var RMSupervisorName    : String? = null,
    @SerializedName("MeetingDescription"  ) var MeetingDescription  : String? = null,
    @SerializedName("MeetingCreationDate" ) var MeetingCreationDate : String? = null,
    @SerializedName("Phone"               ) var Phone               : String? = null
)
