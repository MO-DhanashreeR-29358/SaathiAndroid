package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class TableItem(

  @JvmField @SerializedName("id")
  val id: Int?,

  @JvmField @SerializedName("text")
  val text: String?,

  @JvmField @SerializedName("value")
  val value: String?
)