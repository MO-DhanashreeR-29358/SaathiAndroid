package com.mosl.saathi.api.model.query

import com.google.gson.annotations.SerializedName

data class QueryRequest(

  @JvmField @SerializedName("Status")
  val status: Int = 1,

  @JvmField @SerializedName("Description")
  val description: String,

  @JvmField @SerializedName("Email")
  val email: String,

  @JvmField @SerializedName("mobile")
  val mobile: String,

  @JvmField @SerializedName("PartnerName")
  val partnerName: String,

  @JvmField @SerializedName("BranchCode")
  val branchCode: String
)