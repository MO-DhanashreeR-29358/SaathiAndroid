package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankMeetingSendOTPRequest(
    @JvmField @SerializedName("BankId") val bankId: Int,
    @JvmField @SerializedName("BankBranchCode") val bankBranchCode: String

)