package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class RegisterProspectOnlineRequest(

  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("UCID") val uCID: String
)