package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName

data class TeamTable3Item(

  @JvmField @SerializedName("Ord")
  val ord: Int?,

  @JvmField @SerializedName("DisplayText")
  val displayText: String?,

  @JvmField @SerializedName("Value")
  val value: String?
)