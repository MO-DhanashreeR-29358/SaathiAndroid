package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class ClientViewByCommandRequest(

  @JvmField @SerializedName("UCID") val uCID: String
)