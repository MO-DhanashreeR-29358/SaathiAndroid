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
    @SerializedName("Lead Id"              ) var LeadId              : String? = null,
    @SerializedName("RH Name"              ) var RHName              : String? = null,
    @SerializedName("RM Name"              ) var RMName              : String? = null,
    @SerializedName("Client code"          ) var Clientcode          : String? = null,
    @SerializedName("RM Emp code"           ) var RMEmpcode           : String? = null,
    @SerializedName("Meeting Date"         ) var MeetingDate         : String? = null,
    @SerializedName("Meeting Time"         ) var MeetingTime         : String? = null,
    @SerializedName("Activity Type"        ) var ActivityType        : String? = null,
    @SerializedName("Customer Name"        ) var CustomerName        : String? = null,
    @SerializedName("Attendee Details"     ) var AttendeeDetails     : String? = null,
    @SerializedName("Lead Creation Date"    ) var LeadCreationDate    : String? = null,
    @SerializedName("RM Supervisor Name"    ) var RMSupervisorName    : String? = null,
    @SerializedName("Meeting Description"  ) var MeetingDescription  : String? = null,
    @SerializedName("Meeting Creation Date" ) var MeetingCreationDate : String? = null,
    @SerializedName("Phone"               ) var Phone               : String? = null
)
