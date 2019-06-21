package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class ClientGainLossSummaryResponse(

  @JvmField @SerializedName("Total Gain Loss") val totalGainLoss: List<TotalGainLossItem>,

  @JvmField @SerializedName(
      "Broking Productwise Gainloss"
  ) val brokingProductwiseGainloss: List<BrokingGainLossItem>,

  @JvmField @SerializedName(
      "Asset Productwise Gainloss"
  ) val assetProductwiseGainloss: List<AssetGainLossItem>,

  @JvmField @SerializedName("Available Limits") val availableLimits: List<AvailableLimitsItem>
)