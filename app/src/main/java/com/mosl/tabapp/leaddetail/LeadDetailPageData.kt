package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.model.lead.ContactAddressItem
import com.mosl.tabapp.common.RowData

data class LeadDetail(
  @JvmField val leadOwner: String,
  @JvmField val contactPerson: String,
  @JvmField val sourceId: String,
  @JvmField val leadProspectSourceId: String,
  @JvmField val refereeCode: String,
  @JvmField val refereeName: String,
  @JvmField val isReferee: Boolean,
  @JvmField val profile: String,
  @JvmField val profession: String,
  @JvmField val createdDate: String,
  @JvmField val isPotentialPMSProduct: String = "",
  @JvmField val leadId: String,
  @JvmField val leadCategoryText: String,
  @JvmField val sourceName: String,
  @JvmField val referEmail: String,
  @JvmField val referMobile: String,
  @JvmField val createdBy: String,
  @JvmField val createdThrough: String,
  @JvmField val prospectSource: String,
  @JvmField val lastModifiedBy: String,
  @JvmField val lastModifiedDate: String,
  @JvmField val comments: String,
  @JvmField val leadSourceDetails: String,
  @JvmField val canEditLead: String,
  @JvmField val isProductSpecialist: String,
  @JvmField val prospectReferEmail: String,
  @JvmField val prospectReferName: String//,
    // @JvmField val leadProductMapping: List<LeadProductMap>?,
    //@JvmField val contact: Contact?,
    //@JvmField val leadFieldValue: LeadFieldValue
)

data class LeadProductMap(
  @JvmField val stageId: String,
  @JvmField val statusId: String,
  @JvmField val clientCode: String
)

data class Contact(
  @JvmField val isSameAsAbove: Boolean,
  @JvmField val ucId: Int,
  @JvmField val pan: String,
  @JvmField val aadhar: String,
  @JvmField val ekycMob: String,
  @JvmField val email: String,
  @JvmField val alterEmail: String,
  @JvmField val landline: String,
  @JvmField val alterMob: String,
  @JvmField val mob: String,
  @JvmField val doA: String,
  @JvmField val doB: String,
  @JvmField val name: String,
  @JvmField val address: ContactAddressItem
)

data class LeadFieldValue(
  @JvmField val fieldSectionId: String,
  @JvmField val mandatory: String,
  @JvmField val leadFieldValue: String,
  @JvmField val fieldName: String,
  @JvmField val controlType: String,
  @JvmField val lFSysName: String,
  @JvmField val leadFieldId: String,
  @JvmField val dropdownValue: String
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
  @JvmField val code: String,
  @JvmField val name: String,
  @JvmField val address: String,
  @JvmField val pan: String,
  @JvmField val ucId: String,
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
  @JvmField val overallChangePer: String,
  @JvmField val brokingPortfolios: List<BrokingPortfolio>,
  @JvmField val assetPortfolios: List<AssetPortfolio>,
  @JvmField val cashPortfolios: List<CashPortfolio>
)

data class BrokingPortfolio(
  @JvmField val productId: String,
  @JvmField val productName: String,
  @JvmField val marketValue: String,
  @JvmField val purchaseValue: String,
  @JvmField val overallGainLoss: String,
  @JvmField val percentageGainLoss: String
)

data class AssetPortfolio(
  @JvmField val productId: String,
  @JvmField val productName: String,
  @JvmField val marketValue: String,
  @JvmField val purchaseValue: String,
  @JvmField val overallGainLoss: String,
  @JvmField val percentageGainLoss: String,
  @JvmField val productType: String
)

data class CashPortfolio(
  @JvmField val productName: String,
  @JvmField val totalLedgerBalance: String
)

data class ClientProfitLoss(
  @JvmField val totalLtGainLoss: String,
  @JvmField val totalStGain: String,
  @JvmField val totalGain: String,
  @JvmField val stGainPer: String,
  @JvmField val ltGainPer: String,
  @JvmField val brokingProducts: List<BrokingProduct>,
  @JvmField val assetProducts: List<AssetProduct>,
  @JvmField val availableLimits: List<AvailableLimit>
)

data class BrokingProduct(
  @JvmField val productId: String,
  @JvmField val productName: String,
  @JvmField val stGainLoss: String,
  @JvmField val ltGainLoss: String,
  @JvmField val totalGainLoss: String,
  @JvmField val assetSubType: String
)

data class AssetProduct(
  @JvmField val productId: String,
  @JvmField val productName: String,
  @JvmField val stGainLoss: String,
  @JvmField val ltGainLoss: String,
  @JvmField val totalGainLoss: String
)

data class AvailableLimit(
  @JvmField val cash: String,
  @JvmField val amount: String
)

