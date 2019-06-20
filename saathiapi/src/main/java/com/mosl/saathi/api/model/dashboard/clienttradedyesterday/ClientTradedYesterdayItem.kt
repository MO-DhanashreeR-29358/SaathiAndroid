package com.mosl.saathi.api.model.dashboard.clienttradedyesterday

import com.google.gson.annotations.SerializedName

data class ClientTradedYesterdayItem(

  @JvmField @SerializedName("Table")
  val tables: List<TableItem>?
)