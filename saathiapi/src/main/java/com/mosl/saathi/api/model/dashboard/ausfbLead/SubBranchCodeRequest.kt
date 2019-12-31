package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class SubBranchCodeRequest(
    @JvmField @SerializedName("BankBranchid") val bankBranchId: String
)