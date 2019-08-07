package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class SubBrokerRequest(

  @JvmField @SerializedName("BankId") val bankName: String,

  @JvmField @SerializedName("BankBranchId") val status: String,

  @JvmField @SerializedName("SubBrokerName") val inApproval: String
)