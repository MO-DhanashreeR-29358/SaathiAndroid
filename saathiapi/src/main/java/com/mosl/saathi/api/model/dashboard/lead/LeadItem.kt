package com.mosl.saathi.api.model.dashboard.lead

import com.google.gson.annotations.SerializedName

data class LeadItem(

    @JvmField @SerializedName("id") val id: String,
    @JvmField @SerializedName("stage") val stage: String,
    @JvmField @SerializedName("C0") val C0: String?,
    @JvmField @SerializedName("C1") val C1: String?,
    @JvmField @SerializedName("C2") val C2: String?,
    @JvmField @SerializedName("C3") val C3: String?,
    @JvmField @SerializedName("C4") val C4: String?,
    @JvmField @SerializedName("C5") val C5: String?,
    @JvmField @SerializedName("C6") val C6: String?,
    @JvmField @SerializedName("C7") val C7: String?,
    @JvmField @SerializedName("Total") val total: String?


)