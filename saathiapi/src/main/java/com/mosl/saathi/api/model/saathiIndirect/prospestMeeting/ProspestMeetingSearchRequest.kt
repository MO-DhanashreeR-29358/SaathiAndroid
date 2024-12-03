package com.mosl.saathi.api.model.saathiIndirect.prospestMeeting

import com.google.gson.annotations.SerializedName

data class ProspestMeetingSearchRequest(
    @JvmField @SerializedName("ClientCODE") val clientCODE: String?=null,
    @JvmField @SerializedName("FranchiseeCode") val franchiseeCode: String?=null,
    @JvmField @SerializedName("LeadId") val leadId: String?=null,
    @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
)
