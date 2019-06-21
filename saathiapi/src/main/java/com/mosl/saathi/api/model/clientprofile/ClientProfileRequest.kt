package com.mosl.saathi.api.model.clientprofile

import com.google.gson.annotations.SerializedName

data class ClientProfileRequest(
  @JvmField @SerializedName("ClientPotential") val clientPotential: String,
  @JvmField @SerializedName("IsPotentialAutoUpdated") val isPotentialAutoUpdated: Boolean,
  @JvmField @SerializedName("UCID") val uCID: String
)