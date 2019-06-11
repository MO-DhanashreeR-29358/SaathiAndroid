package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class AssetProductItem(

  @JvmField @SerializedName("MarketValue") val marketValue: String,

  @JvmField @SerializedName("OverallGainLoss") val overallGainLoss: String,

  @JvmField @SerializedName("product") val product: String,

  @JvmField @SerializedName("Assettypeid") val assettypeid: String,

  @JvmField @SerializedName("PercentageGainLoss") val percentageGainLoss: String,

  @JvmField @SerializedName("Producttype") val producttype: String,

  @JvmField @SerializedName("clientname") val clientname: String,

  @JvmField @SerializedName("dividend") val dividend: String,

  @JvmField @SerializedName("ProductID") val productID: String,

  @JvmField @SerializedName("PurchaseValue") val purchaseValue: String
)