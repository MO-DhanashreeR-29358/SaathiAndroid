package com.mosl.saathi.api.model.dashboard.trends

data class TrendsTableItemModel(
  @JvmField
  val parameters: String,
  @JvmField
  val chartType: String,
  @JvmField
  val oct: Float?,
  @JvmField
  val nov: Float?,
  @JvmField
  val dec: Float?,
  @JvmField
  val jan: Float?,
  @JvmField
  val feb: Float?,
  @JvmField
  val mar: Float?

)