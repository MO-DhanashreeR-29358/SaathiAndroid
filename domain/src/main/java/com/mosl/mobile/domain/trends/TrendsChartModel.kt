package com.mosl.mobile.domain.trends

import java.util.LinkedHashMap

data class TrendsChartModel(
  @JvmField
  var legendText: String = "",
  @JvmField
  var chartType: String = "",
  @JvmField
  var monthData: LinkedHashMap<String, Float> = LinkedHashMap()
)