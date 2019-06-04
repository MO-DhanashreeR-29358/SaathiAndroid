package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class LeadDocumentsResponse(

  @JvmField @SerializedName("ID") val id: String?,

  @JvmField @SerializedName("DocumentName") val documentName: String?,

  @JvmField @SerializedName("DocumentType") val documentType: String?,

  @JvmField @SerializedName("DocumentPath") val documentPath: String?,

  @JvmField @SerializedName("CreationDate") val creationDate: String?,

  @JvmField @SerializedName("DocumentFullPath") val documentFullPath: String?

)