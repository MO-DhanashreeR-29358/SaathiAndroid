package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class FiltersItem(

  @field:SerializedName("Operator")
  val operator: String = "startswith",

  @field:SerializedName("Field")
  val field: String = "ClientPotential",

  @field:SerializedName("Value")
  val value: List<String> = listOf("")
)