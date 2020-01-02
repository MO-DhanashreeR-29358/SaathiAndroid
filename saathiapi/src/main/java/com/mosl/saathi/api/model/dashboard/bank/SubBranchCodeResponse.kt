package com.mosl.saathi.api.model.dashboard.bank

import com.google.gson.annotations.SerializedName

data class SubBranchCodeResponse(
    @SerializedName("BankBranchId") val bankBranchId: Int,
    @SerializedName("SubBrokerName") val subBrokerName: String
)