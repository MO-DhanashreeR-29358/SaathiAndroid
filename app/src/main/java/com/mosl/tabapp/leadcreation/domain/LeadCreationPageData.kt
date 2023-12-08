package com.mosl.tabapp.leadcreation.domain

import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.tabapp.common.RowData

data class LeadCreationPageData(
    @JvmField val categoryRowDatas: List<RowData>,
    @JvmField val clientTyoeRowDatas: List<RowData>,
    @JvmField val professionRowDatas: List<RowData>,
    @JvmField val interestInRowDatas: List<RowData>,
    @JvmField val sourceRowDatas: List<RowData>,
    @JvmField val stageRowDatas: List<RowData>,
    @JvmField val countryRowDatas: List<RowData>,
    @JvmField val listBank: List<BankCodeModel>,
    @JvmField val countryStdCodeList: List<RowData>

)

data class LeadDetailPageData(
    @JvmField val profileRowDatas: List<RowData>,
    @JvmField val brokingProfileRowDatas: List<RowData>,
    @JvmField val ipProfileRowDatas: List<RowData>,
    @JvmField val finalProfileRowDatas: List<RowData>,
    @JvmField val interestInRowDatas: List<RowData>,
    @JvmField val countryRowDatas: List<RowData>,
    @JvmField val accountTypeRowDatas: List<RowData>,
    @JvmField val chipsRowDatas: List<RowData>
)

data class SearchReferee(
  @JvmField val code: String,
  @JvmField val name: String,
  @JvmField val email: String,
  @JvmField val mobile: String,
  @JvmField val type: String
)

data class TransferLead(
  @JvmField val leadIdForTransfer: String,
  @JvmField val isPaymentDone: Boolean,
  @JvmField val message: String
)