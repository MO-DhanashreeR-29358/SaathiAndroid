package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class DigitalSalesKitMenu(
  @JvmField @SerializedName("Id") val id: String,
  @JvmField @SerializedName("ParentId") val parentId: String,
  @JvmField @SerializedName("Category") val systemName: String,
  @JvmField @SerializedName("PDFPath") val pdfPath: String
 )

