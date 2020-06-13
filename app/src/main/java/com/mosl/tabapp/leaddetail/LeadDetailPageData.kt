package com.mosl.tabapp.leaddetail

import com.mosl.tabapp.common.RowData

data class LeadDetailPageData(
  @JvmField val professionRowDatas: List<RowData>,
  @JvmField val interestInRowDatas: List<RowData>,
  @JvmField val countryRowDatas: List<RowData>
)

data class LeadDetail(
  @JvmField val leadOwner: String,
  @JvmField val contactPerson: String,
  @JvmField val sourceId: String
    /*@JvmField val leadProspetSourceId: String,*/
    /*@JvmField val refereeCode: String,*/
    /*@JvmField val refereeName: String,*/
    /*@JvmField val isReferee: Boolean,*/
    /*@JvmField val profile: String,*/
    /*@JvmField val profession: String,*/
    /*@JvmField val createdDate: String,*/
    /*@JvmField val isPotentialPMSProduct: Boolean,*/
    /*@JvmField val leadId: String,*/
    /*@JvmField val leadCategoryText: String*/
)

