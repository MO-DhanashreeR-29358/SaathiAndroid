package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class UploadDocumentsRequest(
  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("UCID") val ucid: String,

  @JvmField @SerializedName("DocumentName") val documentName: String,

  @JvmField @SerializedName("Base64FileString") val file: String
)
