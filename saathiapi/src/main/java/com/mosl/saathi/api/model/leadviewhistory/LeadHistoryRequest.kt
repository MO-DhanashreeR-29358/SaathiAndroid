package com.mosl.saathi.api.model.leadviewhistory

import com.google.gson.annotations.SerializedName

data class LeadHistoryRequest(

  @JvmField @SerializedName("Filter")
  val filter: Filter = Filter(),

  @JvmField @SerializedName("Request")
  var request: Request = Request(),

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean = true
)