package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class UpdateDocumentsRequest(

  @JvmField @SerializedName("id") val id: String,

  @JvmField @SerializedName("DocumentName") val documentName: String
)