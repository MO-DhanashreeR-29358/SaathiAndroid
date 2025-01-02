package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName

data class FocoPeriodResponseObjectMain(

    @JvmField @SerializedName("Response")
    val data: FocoPeriodResponseObject
)
data class FocoPeriodResponseObject(

    @JvmField @SerializedName("lstPeriod")
    val list: List<FocoPeriodResponse>
)

data class FocoPeriodResponse(

    @JvmField @SerializedName("Ord")
    val ord: Int,

    @JvmField @SerializedName("DisplayText")
  val displayText: String?,

    @JvmField @SerializedName("Value")
  val value: String?
)

