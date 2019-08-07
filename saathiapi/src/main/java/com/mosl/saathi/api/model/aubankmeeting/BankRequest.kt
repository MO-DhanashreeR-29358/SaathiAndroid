package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankRequest(

  @JvmField @SerializedName("BankName") val bankName: String? = null,

  @JvmField @SerializedName("Status") val status: Int? = null,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean? = null,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: String? = null,

  @JvmField @SerializedName("Permissions") val permissions: String? = null,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean? = null,

  @JvmField @SerializedName("BankId") val bankId: Int? = null
)