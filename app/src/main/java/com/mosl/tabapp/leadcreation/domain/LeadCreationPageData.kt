package com.mosl.tabapp.leadcreation.domain

import com.mosl.tabapp.common.RowData

data class LeadCreationPageData constructor(
  @JvmField val categoryRowDatas: List<RowData>,
  @JvmField val professionRowDatas: List<RowData>
)