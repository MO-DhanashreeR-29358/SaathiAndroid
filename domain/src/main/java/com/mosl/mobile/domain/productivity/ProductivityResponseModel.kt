package com.mosl.mobile.domain.productivity

data class ProductivityData(
  @JvmField val data: List<String>
)

data class ProductivityCombo(
  val headers: List<String>,
  val data: List<ProductivityData>
)

data class ColHeader(
  val label: String,
  val index: Int = 0
)