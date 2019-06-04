package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class LeadSourceListRequest(
    @JvmField @SerializedName("SourceId") val sourceId: String
)