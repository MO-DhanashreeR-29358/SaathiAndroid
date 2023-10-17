package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class AvailableLimitsItem(

  @JvmField @SerializedName("AMOUNT") val aMOUNT: String?,

  @JvmField @SerializedName("CASH") val cASH: String?
)