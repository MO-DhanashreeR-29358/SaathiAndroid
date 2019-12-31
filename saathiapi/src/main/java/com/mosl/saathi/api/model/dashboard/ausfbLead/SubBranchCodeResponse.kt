package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class SubBranchCodeResponse(
    @SerializedName("BankBranchId") val bankBranchId: Int,
    @SerializedName("SubBrokerName") val subBrokerName: String
)