package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StatusRequest(
  @JvmField @SerializedName("StageID") val stageID: String,
  @JvmField @SerializedName("Status") val status: String
)