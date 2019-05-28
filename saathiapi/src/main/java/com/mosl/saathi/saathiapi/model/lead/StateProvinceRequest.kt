package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class StateProvinceRequest(

  @SerializedName("CountryId")
  @JvmField val countryId: String
)