package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageMobileRequest(

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String,

  @SerializedName("StageID")
  @JvmField val stageID: String
)