package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class DeleteDocumentRequest(

  @JvmField @SerializedName("id") val id: String
)