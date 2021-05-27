package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName


data class Table3Item(

  @JvmField @SerializedName("Ord")
  val ord: String?,

  @JvmField @SerializedName("DisplayText")
  val displayText: String?,

  @JvmField @SerializedName("Value")
  val value: String?
)