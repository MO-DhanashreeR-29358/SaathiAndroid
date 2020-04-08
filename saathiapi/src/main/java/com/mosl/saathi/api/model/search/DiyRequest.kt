package com.mosl.saathi.api.model.search

import com.google.gson.annotations.SerializedName

data class DiyRequest(
  @SerializedName("id") @JvmField val leadId: String
)