package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class SurveyLinkRequest(
    @JvmField @SerializedName("LeadId") val leadId: Int,
    @JvmField @SerializedName("ClientCode") val clientCode: String,
    @JvmField @SerializedName("MeetingId") val meetingId: Int,
    @JvmField @SerializedName("RmCode") val rmCode: String,
    @JvmField @SerializedName("Mobile") val mobile: String,
    @JvmField @SerializedName("surveyLinks") val surveyLink: String
)