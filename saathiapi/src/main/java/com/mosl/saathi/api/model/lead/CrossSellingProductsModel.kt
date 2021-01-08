package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class CrossSellingProductResponse(
    @SerializedName("LeadCrossSellingProductId")
    val leadCrossSellingProductId: String,
    @SerializedName("CrossSellingProductId")
    val crossSellingProductId: String,
    @SerializedName("CrossSellingStatusId")
    val crossSellingStatusId: String,
    @SerializedName("CrossSellingProductName")
    val crossSellingProductName: String,
    @SerializedName("CrossSellingStatusName")
    val crossSellingStatusName: String
)