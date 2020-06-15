package com.mosl.tabapp.leaddetail

import com.mosl.tabapp.common.RowData

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

data class LeadDetailAndProfession(
  @JvmField val professionRowDatas: List<RowData>,
  @JvmField val detail: LeadDetail
)

data class ClientDetailWithPortfolioAndProfitLoss(
  @JvmField val detail: ClientDetail,
  @JvmField val portfolio: ClientPortfolio,
  @JvmField val profitLoss: ClientProfitLoss
)

data class ClientDetail(
  @JvmField val name: String,
  @JvmField val address: String,
  @JvmField val pan: String,
  @JvmField val products: List<Product>,
  @JvmField val accountType: String,
  @JvmField val familyUcid: String,
  @JvmField val advisoryType: String,
  @JvmField val aadhar: String,
  @JvmField val account: String,
  @JvmField val profile: String,
  @JvmField val defaultBank: String
)

data class Product(
  @JvmField val channel: String,
  @JvmField val ucId: String,
  @JvmField val branchCode: String,
  @JvmField val clientCode: String,
  @JvmField val advisorCode: String
)

data class ClientPortfolio(
  @JvmField val netWorth: String,
  @JvmField val dayChangeAmount: String,
  @JvmField val dayChangePer: String,
  @JvmField val overallChangeAmt: String,
  @JvmField val overallChangePer: String
)

data class ClientProfitLoss(
  @JvmField val totalLtGainLoss: String,
  @JvmField val totalStGain: String,
  @JvmField val totalGain: String,
  @JvmField val stGainPer: String,
  @JvmField val ltGainPer: String
)

