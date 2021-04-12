package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class MobileListResponse(
    @JvmField @SerializedName("ContactField") val contactField: String,
    @JvmField @SerializedName("ContactNumber") val contactNumber: String
)