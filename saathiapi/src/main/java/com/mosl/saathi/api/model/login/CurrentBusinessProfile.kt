package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class CurrentBusinessProfile(
  @SerializedName("BusinessProfileId") @JvmField val businessProfileId: String?,
  @SerializedName("Name") @JvmField val name: String?,
  @SerializedName("BusinessUnitId") @JvmField val businessUnitId: String?
)