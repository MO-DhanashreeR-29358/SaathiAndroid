package com.mosl.saathi.api.model.ausfbLead

import com.google.gson.annotations.SerializedName

data class BankCodeResponse(
    @SerializedName("BankId") val bankId: Int,
    @SerializedName("BankName") val bankName: String
)