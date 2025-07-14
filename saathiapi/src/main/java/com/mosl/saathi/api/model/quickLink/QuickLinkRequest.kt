package com.mosl.saathi.api.model.quickLink

import com.google.gson.annotations.SerializedName

data class QuickLinkRequest(
  @SerializedName("id") @JvmField val id: String
)