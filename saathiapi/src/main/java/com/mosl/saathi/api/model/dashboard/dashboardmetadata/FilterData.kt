package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class FilterData(

  @JvmField @SerializedName("Table")
  val table: List<TableItem>?
)