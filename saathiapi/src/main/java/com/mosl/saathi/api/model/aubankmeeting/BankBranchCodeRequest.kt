package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankBranchCodeRequest(

  @JvmField @SerializedName("BankBranch") val bankBranch: String,

  @JvmField @SerializedName("BankName") val bankName: String
)