package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class BranchCodeRequest(
    @JvmField @SerializedName("BankId") val bankId: String
)