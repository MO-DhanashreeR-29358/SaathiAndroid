package com.mosl.saathi.api.model.dashboard.bank

import com.google.gson.annotations.SerializedName

data class BankCodeResponse(
    @SerializedName("BankId") val bankId: Int,
    @SerializedName("BankName") val bankName: String
)