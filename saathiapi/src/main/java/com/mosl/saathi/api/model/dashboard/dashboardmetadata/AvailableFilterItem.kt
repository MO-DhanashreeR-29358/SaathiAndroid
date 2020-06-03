package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class AvailableFilterItem(

  @JvmField @SerializedName("FilterSysName")
  val filterSysName: String?,

  @JvmField @SerializedName("KeyValue")
  val keyValue: String?,

  @JvmField @SerializedName("FilterData")
  val filterData: FilterData
)