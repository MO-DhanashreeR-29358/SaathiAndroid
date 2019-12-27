package com.mosl.saathi.api.model.ausfbLead

import com.google.gson.annotations.SerializedName

data class BranchCodeRequest(
    @JvmField @SerializedName("BankId") val bankId: String
)