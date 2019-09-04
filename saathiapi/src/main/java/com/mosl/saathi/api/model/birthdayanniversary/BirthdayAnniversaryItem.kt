package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class BirthdayAnniversaryItem(

  @JvmField @SerializedName("Errors")
  val errors: Any,

  @JvmField @SerializedName("Total")
  val total: Int,

  @JvmField @SerializedName("Permissions")
  val permissions: Permissions,

  @JvmField @SerializedName("Data")
  val data: List<BirthdayAnniversaryData>,

  @JvmField @SerializedName("ExtraData")
  val extraData: Any
)