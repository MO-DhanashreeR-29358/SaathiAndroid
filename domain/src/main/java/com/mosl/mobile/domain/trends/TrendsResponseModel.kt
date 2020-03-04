package com.mosl.saathi.api.model.dashboard.target

import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItemModel

data class TrendsResponseModel(

  @JvmField
  val table: List<TrendsTableItemModel>

)