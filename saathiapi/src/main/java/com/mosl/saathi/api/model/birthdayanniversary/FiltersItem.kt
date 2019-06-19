package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class FiltersItem(

  @JvmField @SerializedName("Operator")
  val operator: String = "startswith",

  @JvmField @SerializedName("Field")
  val field: String = "ClientPotential",

  @JvmField @SerializedName("Value")
  var value: ArrayList<String> = arrayListOf("")
)