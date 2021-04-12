package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class MobileListRequest(
    @JvmField @SerializedName("LeadId") val leadId: String,
    @JvmField @SerializedName("UCID") val ucid: String

)