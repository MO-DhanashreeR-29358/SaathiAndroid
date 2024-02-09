package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class GenerateOtpRequest(
  @JvmField @SerializedName("Mobile") val mobile: String
)