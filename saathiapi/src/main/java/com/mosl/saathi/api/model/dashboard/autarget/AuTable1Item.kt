package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName

data class AuTable1Item(

    @JvmField @SerializedName("formatted")
    val formatted: String?,

    @JvmField @SerializedName("label")
    val label: String?,

    @JvmField @SerializedName("value")
    val value: String?
)