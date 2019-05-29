package com.mosl.saathi.api.model.clientprofile

import com.google.gson.annotations.SerializedName

data class ClientProfileRequest(

  @SerializedName("ClientPotential")
  @JvmField val clientPotential: String,

  @SerializedName("IsPotentialAutoUpdated")
  @JvmField val isPotentialAutoUpdated: Boolean,

  @SerializedName("UCID")
  @JvmField val uCID: String
)