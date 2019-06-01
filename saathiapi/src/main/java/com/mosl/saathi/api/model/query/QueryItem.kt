package com.mosl.saathi.api.model.query

import com.google.gson.annotations.SerializedName

data class QueryItem(

  @JvmField @SerializedName("Status")
  val status: Int,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean,

  @JvmField @SerializedName("Description")
  val description: String,

  @JvmField @SerializedName("Email")
  val email: String,

  @JvmField @SerializedName("QueryId")
  val queryId: Int,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any,

  @JvmField @SerializedName("mobile")
  val mobile: String,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean,

  @JvmField @SerializedName("PartnerName")
  val partnerName: String,

  @JvmField @SerializedName("BranchCode")
  val branchCode: String
)