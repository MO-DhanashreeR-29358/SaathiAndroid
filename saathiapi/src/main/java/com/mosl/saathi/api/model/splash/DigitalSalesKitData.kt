package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class DigitalSalesKitData(
  @JvmField  val id: String,
  @JvmField  val parentId: String,
  @JvmField  val systemName: String,
  @JvmField  val pdfPath: String,
  @JvmField var  menuList: List<DigitalSalesKitData>? = null
 )

