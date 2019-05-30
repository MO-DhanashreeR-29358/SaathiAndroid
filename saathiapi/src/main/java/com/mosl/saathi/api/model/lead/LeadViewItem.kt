package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadViewItem(

  @SerializedName("CreatedThrough")
  @JvmField val createdThrough: String,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("ProspectRefereMobile")
  @JvmField val prospectRefereMobile: String,

  @SerializedName("BusinessUnitID")
  @JvmField val businessUnitID: Int,

  @SerializedName("ContactPerson")
  @JvmField val contactPerson: Any,

  @SerializedName("LeadOwner")
  @JvmField val leadOwner: String,

  @SerializedName("ProspectSource")
  @JvmField val prospectSource: String,

  @SerializedName("PotentialPMSProductList")
  @JvmField val potentialPMSProductList: Any,

  @SerializedName("contact")
  @JvmField val contact: Contact,

  @SerializedName("leadProductMapping")
  @JvmField val leadProductMapping: List<LeadProductMappingItem>,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("IsPotentialPMSProductText")
  @JvmField val isPotentialPMSProductText: Any,

  @SerializedName("StageWiseFieldConfiguration")
  @JvmField val stageWiseFieldConfiguration: List<StageWiseFieldConfigurationItem?>,

  @SerializedName("LastReCycleTransferDate")
  @JvmField val lastReCycleTransferDate: Any,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: String,

  @SerializedName("RefereeEmail")
  @JvmField val refereeEmail: Any,

  @SerializedName("OAOUserid")
  @JvmField val oAOUserid: Int,

  @SerializedName("QueryId")
  @JvmField val queryId: Int,

  @SerializedName("ProspectRefereEmail")
  @JvmField val prospectRefereEmail: String,

  @SerializedName("ChannelID")
  @JvmField val channelID: Any,

  @SerializedName("IsProductSpecialist")
  @JvmField val isProductSpecialist: String,

  @SerializedName("IsAadhar")
  @JvmField val isAadhar: Int,

  @SerializedName("LeadCategoryText")
  @JvmField val leadCategoryText: String,

  @SerializedName("Last_Recycle_Date")
  @JvmField val lastRecycleDate: Any,

  @SerializedName("Branchcode")
  @JvmField val branchcode: String,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("Profession")
  @JvmField val profession: String,

  @SerializedName("ProspectRefereName")
  @JvmField val prospectRefereName: String,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: String,

  @SerializedName("ProfileParamPercentage")
  @JvmField val profileParamPercentage: Int,

  @SerializedName("CanEditLead")
  @JvmField val canEditLead: Int,

  @SerializedName("Is_Recycle_Lead")
  @JvmField val isRecycleLead: Any,

  @SerializedName("RefereeMobile")
  @JvmField val refereeMobile: Any,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: String,

  @SerializedName("RefereeName")
  @JvmField val refereeName: Any,

  @SerializedName("ProfessionID")
  @JvmField val professionID: Any,

  @SerializedName("CampaignName")
  @JvmField val campaignName: Any,

  @SerializedName("Profile")
  @JvmField val profile: Any,

  @SerializedName("LeadSourceDetails")
  @JvmField val leadSourceDetails: Any,

  @SerializedName("MoneyControlPortfolioURL")
  @JvmField val moneyControlPortfolioURL: String,

  @SerializedName("AvailableAddressTypes")
  @JvmField val availableAddressTypes: List<Any?>,

  @SerializedName("LeadSourceName")
  @JvmField val leadSourceName: Any,

  @SerializedName("leadFieldSectionModels")
  @JvmField val leadFieldSectionModels: List<Any?>,

  @SerializedName("ProfileID")
  @JvmField val profileID: Int,

  @SerializedName("DedupeModel")
  @JvmField val dedupeModel: DedupeModel,

  @SerializedName("LoginId")
  @JvmField val loginId: Any,

  @SerializedName("ApprovalMessage")
  @JvmField val approvalMessage: Any,

  @SerializedName("Comments")
  @JvmField val comments: String,

  @SerializedName("SourceID")
  @JvmField val sourceID: Int,

  @SerializedName("RefereeCode")
  @JvmField val refereeCode: Any,

  @SerializedName("SourceName")
  @JvmField val sourceName: String,

  @SerializedName("LeadSourceID")
  @JvmField val leadSourceID: Int,

  @SerializedName("ContactID")
  @JvmField val contactID: Int,

  @SerializedName("LeadSourceDetailsID")
  @JvmField val leadSourceDetailsID: Int,

  @SerializedName("AMCCampaignID")
  @JvmField val aMCCampaignID: Int,

  @SerializedName("IsAllowed")
  @JvmField val isAllowed: Boolean,

  @SerializedName("IsPotentialPMSProductValue")
  @JvmField val isPotentialPMSProductValue: String,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("IsReCycleTransfer")
  @JvmField val isReCycleTransfer: Any,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LeadID")
  @JvmField val leadID: Int,

  @SerializedName("IsPotentialPMSProduct")
  @JvmField val isPotentialPMSProduct: String,

  @SerializedName("leadProfileParameters")
  @JvmField val leadProfileParameters: Any,

  @SerializedName("LeadCategoryID")
  @JvmField val leadCategoryID: Int,

  @SerializedName("leadFieldValueModels")
  @JvmField val leadFieldValueModels: List<Any?>
)