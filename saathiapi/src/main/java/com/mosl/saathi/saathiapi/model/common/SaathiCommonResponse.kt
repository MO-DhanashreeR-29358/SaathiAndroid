package com.mosl.saathi.saathiapi.model.common

import com.google.gson.annotations.SerializedName

data class SaathiCommonResponse<T>(

  @SerializedName("Response")
  @JvmField val response: List<T>,

  @SerializedName("Status")
  @JvmField val status: Boolean,

  @SerializedName("Type")
  @JvmField val type: String,

  @SerializedName("Message")
  @JvmField val message: String,

  @SerializedName("Erros")
  @JvmField val erros: List<Any>
)