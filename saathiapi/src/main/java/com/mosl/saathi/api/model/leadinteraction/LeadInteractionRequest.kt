package com.mosl.saathi.api.model.leadinteraction

import com.google.gson.annotations.SerializedName

data class LeadInteractionRequest(

  @JvmField @SerializedName("Filter")
  val filter: Filter = Filter(),

  @JvmField @SerializedName("Request")
  val request: Request = Request(),

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean = true
)