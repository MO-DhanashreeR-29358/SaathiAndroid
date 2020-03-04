package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName
import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItem

data class TrendsResponse(

  @JvmField @SerializedName("Table")
  val table: List<TrendsTableItem>
)