package com.mosl.tabapp.saathiIndirect.LeadCreation.domain

import com.mosl.tabapp.common.RowData

data class IndirectLeadCreationPageData(
    @JvmField val categoryRowDatas: List<RowData>,
    @JvmField val interestInRowDatas: List<RowData>,
    @JvmField val sourceRowDatas: List<RowData>,
    @JvmField val stageRowDatas: List<RowData>,
    @JvmField val countryRowDatas: List<RowData>

)

data class IndirectLeadDetailPageData(
    @JvmField val categoryRowDatas: List<RowData>,
    @JvmField val interestInRowDatas: List<RowData>,
    @JvmField val sourceRowDatas: List<RowData>,
    @JvmField val stageRowDatas: List<RowData>,
    @JvmField val countryRowDatas: List<RowData>
)

data class IndirectSearchReferee(
  @JvmField val code: String,
  @JvmField val name: String,
  @JvmField val email: String,
  @JvmField val mobile: String,
  @JvmField val type: String
)

data class IndirectTransferLead(
  @JvmField val leadIdForTransfer: String,
  @JvmField val isPaymentDone: Boolean,
  @JvmField val message: String
)