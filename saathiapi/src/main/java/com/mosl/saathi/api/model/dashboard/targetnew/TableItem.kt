package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName

data class TableItem(

    @JvmField @SerializedName("Ord")
    val ord: Int,

    @JvmField @SerializedName("DisplayText")
  val displayText: String?,

    @JvmField @SerializedName("Value")
  val value: String?
)

