package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class CashPortfolioItem(

  @JvmField @SerializedName("ProductType") val productType: String,

  @JvmField @SerializedName("TotalLedgerBalance") val totalLedgerBalance: String,

  @JvmField @SerializedName("Product") val product: String
)