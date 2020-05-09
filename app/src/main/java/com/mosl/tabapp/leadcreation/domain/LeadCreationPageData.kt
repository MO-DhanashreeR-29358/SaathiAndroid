package com.mosl.tabapp.leadcreation.domain

import com.mosl.tabapp.common.RowData

data class LeadCreationPageData constructor(
  @JvmField val categoryRowDatas: List<RowData>,
  @JvmField val professionRowDatas: List<RowData>,
  @JvmField val interestInRowDatas: List<RowData>,
  @JvmField val sourceRowDatas: List<RowData>,
  @JvmField val stageRowDatas: List<RowData>,
  @JvmField val countryRowDatas: List<RowData>
)