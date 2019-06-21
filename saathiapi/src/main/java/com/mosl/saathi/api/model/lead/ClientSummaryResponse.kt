package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class ClientSummaryResponse(

  @JvmField @SerializedName("Broking Product") val brokingProduct: List<BrokingProductItem>,

  @JvmField @SerializedName("Asset Product") val assetProduct: List<AssetProductItem>,

  @JvmField @SerializedName(
      "Portfolio Snapshot"
  ) val portfolioSnapshot: List<PortfolioSnapshotItem>,

  @JvmField @SerializedName("Cash Portfolio") val cashPortfolio: List<CashPortfolioItem>
)