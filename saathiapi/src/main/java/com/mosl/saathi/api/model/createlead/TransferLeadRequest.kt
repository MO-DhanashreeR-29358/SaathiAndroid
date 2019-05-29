package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class TransferLeadRequest(

  @JvmField @SerializedName("CurrentUser") val currentUser: String,

  @JvmField @SerializedName("Mobile") val mobile: String
)