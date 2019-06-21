package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class ClientDetailRequest(
  @JvmField @SerializedName("UCID") val uCID: Int
)