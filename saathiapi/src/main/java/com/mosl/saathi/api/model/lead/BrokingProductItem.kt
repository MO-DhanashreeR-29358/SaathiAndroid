package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class BrokingProductItem(

  @JvmField @SerializedName("product") val product: String,

  @JvmField @SerializedName("MarketValue") val marketValue: String,

  @JvmField @SerializedName("PurchaseValue") val purchaseValue: String,

  @JvmField @SerializedName("OverallGainLoss") val overallGainLoss: String,

  @JvmField @SerializedName("PercentageGainLoss") val percentageGainLoss: String,

  @JvmField @SerializedName("ProductID") val productId: String
)