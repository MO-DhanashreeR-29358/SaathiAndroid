package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class UploadDocumentItem(

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("CreationDate") val creationDate: String?,

  @JvmField @SerializedName("CreatedBy") val createdBy: Any?,

  @JvmField @SerializedName("DocumentType") val documentType: String?,

  @JvmField @SerializedName("DocumentPath") val documentPath: String?,

  @JvmField @SerializedName("DocumentFullPath") val documentFullPath: String?,

  @JvmField @SerializedName("LeadID") val leadID: Int?,

  @JvmField @SerializedName("ID") val iD: String?,

  @JvmField @SerializedName("UCID") val uCID: Int?,

  @JvmField @SerializedName("DocumentName") val documentName: String?,

  @JvmField @SerializedName("Base64FileString") val base64FileString: Any?
)