package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankBranchCodeResponse(

    @JvmField @SerializedName("Bankname") val bankName: String,

    @JvmField @SerializedName("BankBranchId") val bankBranchId: Int

)
