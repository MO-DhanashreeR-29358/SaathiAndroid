package com.mosl.saathi.api.model.clientviewhistory

import com.google.gson.annotations.SerializedName

data class ClientHistoryRequest(

  @JvmField @SerializedName("Filter")
  val filter: Filter = Filter(),

  @JvmField @SerializedName("Request")
  var request: Request = Request(),

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean? = true
)