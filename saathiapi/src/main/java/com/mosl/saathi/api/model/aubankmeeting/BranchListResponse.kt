package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BranchListResponse(

  @JvmField @SerializedName("BranchName") val branchName: String,

  @JvmField @SerializedName("BankBranchId") val bankBranchId: Int,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("BankId") val bankId: Int
)