package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class SaveLeadProfileRequest(

  @JvmField @SerializedName("ProfileParameterID")
  val profileParameterID: String,

  @JvmField @SerializedName("ProfileSubParameterID")
  val profileSubParameterID: String,

  @JvmField @SerializedName("LeadID")
  val leadID: String,

  @JvmField @SerializedName("Value")
  val value: String

)