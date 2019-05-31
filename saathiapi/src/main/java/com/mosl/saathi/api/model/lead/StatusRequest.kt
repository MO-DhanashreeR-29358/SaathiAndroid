package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StatusRequest(

  @SerializedName("Status")
  @JvmField val status: String,

  @SerializedName("StageID")
  @JvmField val stageID: String
)