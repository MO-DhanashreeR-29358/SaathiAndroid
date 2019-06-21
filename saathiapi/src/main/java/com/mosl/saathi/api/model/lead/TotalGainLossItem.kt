package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class TotalGainLossItem(

  @JvmField @SerializedName("TOTALST_GAIN") val tOTALSTGAIN: String?,

  @JvmField @SerializedName("TOTAL_GAIN") val tOTALGAIN: String?,

  @JvmField @SerializedName("LT_GAIN_PER") val lTGAINPER: String?,

  @JvmField @SerializedName("ST_GAINUpdown") val sTGAINUpdown: String,

  @JvmField @SerializedName("ST_GAIN_PER") val sTGAINPER: String?,

  @JvmField @SerializedName("TOTAL_GAIN_PER") val tOTALGAINPER: String,

  @JvmField @SerializedName("LT_GAINUpdown") val lTGAINUpdown: String,

  @JvmField @SerializedName("TOTALLT_GAIN") val tOTALLTGAIN: String?
)