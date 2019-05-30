package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class ClientDetailRequest(
  @SerializedName("UCID")
  @JvmField val uCID: String
)