package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class DocumentsRequest(
  @JvmField @SerializedName("Request") val request: LeadDocumentsRequest,
  @JvmField @SerializedName("Filter") val filter: FilterRequest,
  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean
)