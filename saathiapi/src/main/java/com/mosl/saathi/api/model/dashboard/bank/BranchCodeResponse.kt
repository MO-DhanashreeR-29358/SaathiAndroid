package com.mosl.saathi.api.model.dashboard.bank

import com.google.gson.annotations.SerializedName

data class BranchCodeResponse(
    @SerializedName("BankId") val bankId: Int,
    @SerializedName("BranchName") val branchName: String,
    @SerializedName("BranchId") val branchId: Int
)