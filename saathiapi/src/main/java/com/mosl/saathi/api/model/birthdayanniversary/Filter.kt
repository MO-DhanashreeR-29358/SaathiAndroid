package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class Filter(

  @JvmField @SerializedName("Filters")
  val filters: List<FiltersItem?>,

  @JvmField @SerializedName("Logic")
  val logic: String = "and"
)