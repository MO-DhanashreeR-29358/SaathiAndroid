package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class ClientByCommandRequest(

  @JvmField @SerializedName("UCID")
  val uCID: String
)