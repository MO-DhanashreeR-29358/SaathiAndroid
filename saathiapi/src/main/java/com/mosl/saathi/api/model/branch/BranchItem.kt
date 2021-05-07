package com.mosl.saathi.api.model.branch

import com.google.gson.annotations.SerializedName

data class BranchItem(

    @JvmField @SerializedName("BranchCode")
    val branchCode: String

)