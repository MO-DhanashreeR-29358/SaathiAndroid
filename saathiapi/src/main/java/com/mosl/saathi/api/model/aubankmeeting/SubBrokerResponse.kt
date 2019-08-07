package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class SubBrokerResponse(

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("SubBrokerName") val subBrokerName: String,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("BankSubBrokerId") val bankSubBrokerId: Int,

  @JvmField @SerializedName("BankBranchId") val bankBranchId: Int,

  @JvmField @SerializedName("BankId") val bankId: Int
)