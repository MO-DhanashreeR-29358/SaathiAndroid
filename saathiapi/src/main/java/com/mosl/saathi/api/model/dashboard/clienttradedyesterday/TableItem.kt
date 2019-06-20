package com.mosl.saathi.api.model.dashboard.clienttradedyesterday

import com.google.gson.annotations.SerializedName

data class TableItem(

  @JvmField @SerializedName("REVENUE_GENERATED")
  val rEVENUEGENERATED: Int?,

  @JvmField @SerializedName("NO_OF_CLIENTS")
  val nOOFCLIENTS: Int?,

  @JvmField @SerializedName("RevenueType")
  val revenueType: String?
)