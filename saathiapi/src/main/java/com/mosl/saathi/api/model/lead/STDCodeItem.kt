package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class STDCodeItem(

  @SerializedName("Id")
  @JvmField val id: Int,

  @SerializedName("STDCode")
  @JvmField val STDCodeDisplay: String,

  @SerializedName("STDCodeValue")
  @JvmField val STDCode: String
)