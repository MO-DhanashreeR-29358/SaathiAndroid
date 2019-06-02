package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadViewRequest(
    @JvmField @SerializedName("id") val id: String
)