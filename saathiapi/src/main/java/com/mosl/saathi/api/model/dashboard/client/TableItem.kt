package com.mosl.saathi.api.model.dashboard.client

import com.google.gson.annotations.SerializedName

data class TableItem(

  @JvmField @SerializedName("DAY_CNT")
  val dAYCNT: String,

  @JvmField @SerializedName("FAMILYSTATUS")
  val fAMILYSTATUS: String,

  @JvmField @SerializedName("MTD_CNT")
  val mTDCNT: String,

  @JvmField @SerializedName("WTD_PER")
  val wTDPER: String,

  @JvmField @SerializedName("MTD_PER")
  val mTDPER: String,

  @JvmField @SerializedName("YTD_PER")
  val yTDPER: String,

  @JvmField @SerializedName("WTD_CNT")
  val wTDCNT: String,

  @JvmField @SerializedName("DAY_PER")
  val dAYPER: String,

  @JvmField @SerializedName("YTD_CNT")
  val yTDCNT: String
)