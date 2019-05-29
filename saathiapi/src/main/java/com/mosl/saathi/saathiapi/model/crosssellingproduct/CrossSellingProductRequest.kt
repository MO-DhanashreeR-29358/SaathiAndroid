package com.mosl.saathi.saathiapi.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class CrossSellingProductRequest(

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String,

  @SerializedName("InterestedInId")
  val interestedInId: String
)