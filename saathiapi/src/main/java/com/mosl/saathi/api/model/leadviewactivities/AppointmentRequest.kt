package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class AppointmentRequest(

  @field:SerializedName("Filter")
  val filter: Filter? = null,

  @field:SerializedName("Request")
  val request: Request? = null,

  @field:SerializedName("IsAdminMode")
  val isAdminMode: Boolean? = null
)