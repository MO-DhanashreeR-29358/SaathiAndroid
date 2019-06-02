package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class SaveLeadCrossSellingProductRequest @JvmOverloads constructor(
    @JvmField @SerializedName("LeadId") val leadId: String,
    @JvmField @SerializedName("InterestedInId") val interestedInId: String,
    @JvmField @SerializedName("CrossSellingStatusId") val crossSellingStatusId: String,
    @JvmField @SerializedName("CrossSellingProductId") val crossSellingProductId: String,
    @JvmField @SerializedName("LeadCrossSellingProductId") val leadCrossSellingProductId: String? = null
)