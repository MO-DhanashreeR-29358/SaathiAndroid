package com.mosl.saathi.api.model.clientprofile

import com.google.gson.annotations.SerializedName

data class ClientProfileItem(

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("AdvisoryType")
  @JvmField val advisoryType: Any,

  @SerializedName("Address")
  @JvmField val address: Any,

  @SerializedName("RegionCode")
  @JvmField val regionCode: Any,

  @SerializedName("OfficeCountry")
  @JvmField val officeCountry: Any,

  @SerializedName("Count")
  @JvmField val count: Int,

  @SerializedName("AccountOpeningDate")
  @JvmField val accountOpeningDate: Any,

  @SerializedName("UCID")
  @JvmField val uCID: Int,

  @SerializedName("FirstTrade")
  @JvmField val firstTrade: Any,

  @SerializedName("DrishtiHome")
  @JvmField val drishtiHome: Any,

  @SerializedName("DOB")
  @JvmField val dOB: Any,

  @SerializedName("IFAMappingDate")
  @JvmField val iFAMappingDate: Any,

  @SerializedName("PremailingAddress")
  @JvmField val premailingAddress: Any,

  @SerializedName("FamilyProducts")
  @JvmField val familyProducts: Any,

  @SerializedName("DrishtiEmail1")
  @JvmField val drishtiEmail1: Any,

  @SerializedName("DrishtiEmail2")
  @JvmField val drishtiEmail2: Any,

  @SerializedName("InApprovalProcessCount")
  @JvmField val inApprovalProcessCount: Int,

  @SerializedName("ClientPotential")
  @JvmField val clientPotential: String,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: Any,

  @SerializedName("productId")
  @JvmField val productId: Int,

  @SerializedName("IndirectClientStatus")
  @JvmField val indirectClientStatus: Any,

  @SerializedName("TraderCode")
  @JvmField val traderCode: Any,

  @SerializedName("AdvisoryType_ProcessType")
  @JvmField val advisoryTypeProcessType: Any,

  @SerializedName("CustomerClassification")
  @JvmField val customerClassification: Any,

  @SerializedName("DrishtiOffice")
  @JvmField val drishtiOffice: Any,

  @SerializedName("successfullRecords")
  @JvmField val successfullRecords: Int,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("IFAUCID")
  @JvmField val iFAUCID: Any,

  @SerializedName("Country")
  @JvmField val country: Any,

  @SerializedName("CurrentYearRevenue")
  @JvmField val currentYearRevenue: Int,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("ClientType")
  @JvmField val clientType: Any,

  @SerializedName("SelectedUCIDs")
  @JvmField val selectedUCIDs: Any,

  @SerializedName("LastTwoYearRevenue")
  @JvmField val lastTwoYearRevenue: Int,

  @SerializedName("Profile")
  @JvmField val profile: Int,

  @SerializedName("PinCode")
  @JvmField val pinCode: Any,

  @SerializedName("PrimaryTrainer")
  @JvmField val primaryTrainer: Any,

  @SerializedName("PANNumber")
  @JvmField val pANNumber: Any,

  @SerializedName("ReadOnlyAccess")
  @JvmField val readOnlyAccess: Boolean,

  @SerializedName("PrefixCode")
  @JvmField val prefixCode: Any,

  @SerializedName("SignedOffDate")
  @JvmField val signedOffDate: Any,

  @SerializedName("UserBranches")
  @JvmField val userBranches: Any,

  @SerializedName("UserProducts")
  @JvmField val userProducts: Any,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: Any,

  @SerializedName("ApprovalMessage")
  @JvmField val approvalMessage: Any,

  @SerializedName("RetailClientType")
  @JvmField val retailClientType: Any,

  @SerializedName("Mobile")
  @JvmField val mobile: Any,

  @SerializedName("RegionalManager")
  @JvmField val regionalManager: Any,

  @SerializedName("ClientMappingType")
  @JvmField val clientMappingType: Any,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ActivationDeskComments")
  @JvmField val activationDeskComments: Any,

  @SerializedName("InternalReferral")
  @JvmField val internalReferral: Any,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("ChannelType")
  @JvmField val channelType: Any,

  @SerializedName("AltEmailAddress")
  @JvmField val altEmailAddress: Any,

  @SerializedName("RecCreationDate")
  @JvmField val recCreationDate: Any,

  @SerializedName("IsFamilyHead")
  @JvmField val isFamilyHead: Boolean,

  @SerializedName("OfficePhone")
  @JvmField val officePhone: Any,

  @SerializedName("Products")
  @JvmField val products: Any,

  @SerializedName("IsAdmin")
  @JvmField val isAdmin: Boolean,

  @SerializedName("Email")
  @JvmField val email: Any,

  @SerializedName("Processtype")
  @JvmField val processtype: Int,

  @SerializedName("SearchUCID")
  @JvmField val searchUCID: Any,

  @SerializedName("Stock_15_Days_Gross_Margin")
  @JvmField val stock15DaysGrossMargin: Int,

  @SerializedName("ClientRevenue")
  @JvmField val clientRevenue: Int,

  @SerializedName("FamilyUcidStatus")
  @JvmField val familyUcidStatus: Any,

  @SerializedName("ISNormalAdvisor")
  @JvmField val iSNormalAdvisor: Boolean,

  @SerializedName("WMChannelType")
  @JvmField val wMChannelType: Any,

  @SerializedName("ClientName")
  @JvmField val clientName: Any,

  @SerializedName("Ranking")
  @JvmField val ranking: Any,

  @SerializedName("ManualMapping")
  @JvmField val manualMapping: Boolean,

  @SerializedName("ClientSegment")
  @JvmField val clientSegment: Any,

  @SerializedName("AUM")
  @JvmField val aUM: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("TerritoryManager")
  @JvmField val territoryManager: Any,

  @SerializedName("SubBrokerCode")
  @JvmField val subBrokerCode: Any,

  @SerializedName("UnsuccessfullRecords")
  @JvmField val unsuccessfullRecords: Int,

  @SerializedName("Channel")
  @JvmField val channel: Any,

  @SerializedName("City")
  @JvmField val city: Any,

  @SerializedName("ProfileClientCategory")
  @JvmField val profileClientCategory: Any,

  @SerializedName("SecondaryTrainer")
  @JvmField val secondaryTrainer: Any,

  @SerializedName("AadhaarNumber")
  @JvmField val aadhaarNumber: Any,

  @SerializedName("PotentialAutoUpdatedDate")
  @JvmField val potentialAutoUpdatedDate: Any,

  @SerializedName("IsMappedToDrishti")
  @JvmField val isMappedToDrishti: Boolean,

  @SerializedName("OfficeState")
  @JvmField val officeState: Any,

  @SerializedName("IsPoolAdvisor")
  @JvmField val isPoolAdvisor: Boolean,

  @SerializedName("State")
  @JvmField val state: Any,

  @SerializedName("OfficePinCode")
  @JvmField val officePinCode: Any,

  @SerializedName("AreaCode")
  @JvmField val areaCode: Any,

  @SerializedName("IFAName")
  @JvmField val iFAName: Any,

  @SerializedName("BranchCode")
  @JvmField val branchCode: Any,

  @SerializedName("ResidentialPhone")
  @JvmField val residentialPhone: Any,

  @SerializedName("BulkMapType")
  @JvmField val bulkMapType: Any,

  @SerializedName("FamilyUCID")
  @JvmField val familyUCID: Int,

  @SerializedName("LastTradingDate")
  @JvmField val lastTradingDate: Any,

  @SerializedName("ClientAcquirer")
  @JvmField val clientAcquirer: Any,

  @SerializedName("DrishtiMobile4")
  @JvmField val drishtiMobile4: Any,

  @SerializedName("DrishtiMobile3")
  @JvmField val drishtiMobile3: Any,

  @SerializedName("DrishtiMobile2")
  @JvmField val drishtiMobile2: Any,

  @SerializedName("Message")
  @JvmField val message: Any,

  @SerializedName("DrishtiMobile1")
  @JvmField val drishtiMobile1: Any,

  @SerializedName("AniversaryDate")
  @JvmField val aniversaryDate: Any,

  @SerializedName("Rating")
  @JvmField val rating: Any,

  @SerializedName("DemoDone")
  @JvmField val demoDone: Any,

  @SerializedName("SelectAll")
  @JvmField val selectAll: Boolean,

  @SerializedName("CountType")
  @JvmField val countType: Any,

  @SerializedName("DPValue")
  @JvmField val dPValue: Int,

  @SerializedName("OfficeCity")
  @JvmField val officeCity: Any,

  @SerializedName("DowngradedOn")
  @JvmField val downgradedOn: String,

  @SerializedName("PoolClientCount")
  @JvmField val poolClientCount: Int,

  @SerializedName("Stock_60_Days_Gross_Margin")
  @JvmField val stock60DaysGrossMargin: Int,

  @SerializedName("ClientCategory")
  @JvmField val clientCategory: Any,

  @SerializedName("AdvisoryBranchCode")
  @JvmField val advisoryBranchCode: Any,

  @SerializedName("ClientInterest")
  @JvmField val clientInterest: Any,

  @SerializedName("IsPotentialAutoUpdated")
  @JvmField val isPotentialAutoUpdated: Boolean,

  @SerializedName("ClientStatus")
  @JvmField val clientStatus: Any,

  @SerializedName("AdvisoryTypeBranchCode")
  @JvmField val advisoryTypeBranchCode: Any,

  @SerializedName("OfficeAddress")
  @JvmField val officeAddress: Any,

  @SerializedName("AccountType")
  @JvmField val accountType: Any,

  @SerializedName("InitialCashMargin")
  @JvmField val initialCashMargin: Any,

  @SerializedName("Mobile2")
  @JvmField val mobile2: Any,

  @SerializedName("StockSectorPref")
  @JvmField val stockSectorPref: Any,

  @SerializedName("ClientCode")
  @JvmField val clientCode: Any,

  @SerializedName("EffectiveFrom")
  @JvmField val effectiveFrom: Any,

  @SerializedName("Fax")
  @JvmField val fax: Any
)