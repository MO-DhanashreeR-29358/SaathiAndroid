package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationSummaryResponse(

    @JvmField @SerializedName("Data")
    val data: List<ReactivationSummaryData>

)

data class ReactivationSummaryData(

    @JvmField @SerializedName("Assigned")
    val assigned: String,
    @JvmField @SerializedName("Contacted")
    val contacted: String,
    @JvmField @SerializedName("NotContacted")
    val nonContacted: String,
    @JvmField @SerializedName("ReactivatedClients")
    val reactivatedClients: String,
    @JvmField @SerializedName("Traded")
    val traded: String,
    @JvmField @SerializedName("Revenue")
    val revenue: String
    )