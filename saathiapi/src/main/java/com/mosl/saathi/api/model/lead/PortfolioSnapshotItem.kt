package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class PortfolioSnapshotItem(

  @JvmField @SerializedName("OverallChangeAmount") val overallChangeAmount: String?,

  @JvmField @SerializedName("Networth") val networth: String?,

  @JvmField @SerializedName("OverallChangeUpdown") val overallChangeUpdown: String,

  @JvmField @SerializedName("DayChangePercentage") val dayChangePercentage: String,

  @JvmField @SerializedName("DayChangeAmount") val dayChangeAmount: String?,

  @JvmField @SerializedName("DayChangeUpdown") val dayChangeUpdown: String,

  @JvmField @SerializedName("OverallChangePercentage") val overallChangePercentage: String?
)