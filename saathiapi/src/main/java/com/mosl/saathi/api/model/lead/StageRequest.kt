package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageRequest(
    @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
    @JvmField @SerializedName("StageID") val stageID: String? = null
)