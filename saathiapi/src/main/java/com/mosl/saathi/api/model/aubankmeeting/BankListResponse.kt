package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankListResponse(

  @JvmField @SerializedName("BankName") val bankName: String,

  @JvmField @SerializedName("BankId") val bankId: Int,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any? = null,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean
)