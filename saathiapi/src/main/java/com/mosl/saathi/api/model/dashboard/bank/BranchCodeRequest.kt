package com.mosl.saathi.api.model.dashboard.bank

import com.google.gson.annotations.SerializedName

data class BranchCodeRequest(
    @JvmField @SerializedName("BankId") val bankId: String
)