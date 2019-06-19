package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class BirthdayAnniversaryRequest(

  @JvmField @SerializedName("Filter")
  var filter: Filter,

  @JvmField @SerializedName("Request")
  val request: Request,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean = true
)