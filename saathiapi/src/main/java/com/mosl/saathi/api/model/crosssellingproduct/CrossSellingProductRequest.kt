package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class CrossSellingProductRequest(
    @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
    @JvmField @SerializedName("InterestedInId") val interestedInId: String
)