package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class AddUpdateLeadItem(

  @JvmField @SerializedName("CreatedThrough")
  val createdThrough: String,

  @JvmField @SerializedName("CreationDate")
  val creationDate: String,

  @JvmField @SerializedName("ProspectRefereMobile")
  val prospectRefereMobile: String,

  @JvmField @SerializedName("BusinessUnitID")
  val businessUnitID: Int,

  @JvmField @SerializedName("ContactPerson")
  val contactPerson: Any,

  @JvmField @SerializedName("LeadOwner")
  val leadOwner: Any,

  @JvmField @SerializedName("ProspectSource")
  val prospectSource: Any,

  @JvmField @SerializedName("PotentialPMSProductList")
  val potentialPMSProductList: Any,

  @JvmField @SerializedName("contact")
  val contact: Contact,

  @JvmField @SerializedName("leadProductMapping")
  val leadProductMapping: List<LeadProductMappingItem?>,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean,

  @JvmField @SerializedName("IsPotentialPMSProductText")
  val isPotentialPMSProductText: Any,

  @JvmField @SerializedName("StageWiseFieldConfiguration")
  val stageWiseFieldConfiguration: List<Any?>,

  @JvmField @SerializedName("LastReCycleTransferDate")
  val lastReCycleTransferDate: Any,

  @JvmField @SerializedName("Status")
  val status: Int,

  @JvmField @SerializedName("CreatedBy")
  val createdBy: String,

  @JvmField @SerializedName("RefereeEmail")
  val refereeEmail: Any,

  @JvmField @SerializedName("OAOUserid")
  val oAOUserid: Any,

  @JvmField @SerializedName("QueryId")
  val queryId: Any,

  @JvmField @SerializedName("ProspectRefereEmail")
  val prospectRefereEmail: String,

  @JvmField @SerializedName("ChannelID")
  val channelID: Any,

  @JvmField @SerializedName("IsProductSpecialist")
  val isProductSpecialist: Any,

  @JvmField @SerializedName("IsAadhar")
  val isAadhar: Any,

  @JvmField @SerializedName("LeadCategoryText")
  val leadCategoryText: Any,

  @JvmField @SerializedName("Last_Recycle_Date")
  val lastRecycleDate: Any,

  @JvmField @SerializedName("Branchcode")
  val branchcode: Any,

  @JvmField @SerializedName("Active")
  val active: Boolean,

  @JvmField @SerializedName("Profession")
  val profession: Any,

  @JvmField @SerializedName("ProspectRefereName")
  val prospectRefereName: String,

  @JvmField @SerializedName("LastModificationDate")
  val lastModificationDate: Any,

  @JvmField @SerializedName("ProfileParamPercentage")
  val profileParamPercentage: Any,

  @JvmField @SerializedName("CanEditLead")
  val canEditLead: Int,

  @JvmField @SerializedName("Is_Recycle_Lead")
  val isRecycleLead: Any,

  @JvmField @SerializedName("RefereeMobile")
  val refereeMobile: Any,

  @JvmField @SerializedName("LastModifiedBy")
  val lastModifiedBy: Any,

  @JvmField @SerializedName("RefereeName")
  val refereeName: Any,

  @JvmField @SerializedName("ProfessionID")
  val professionID: Int,

  @JvmField @SerializedName("CampaignName")
  val campaignName: Any,

  @JvmField @SerializedName("Profile")
  val profile: Any,

  @JvmField @SerializedName("LeadSourceDetails")
  val leadSourceDetails: String,

  @JvmField @SerializedName("MoneyControlPortfolioURL")
  val moneyControlPortfolioURL: Any,

  @JvmField @SerializedName("AvailableAddressTypes")
  val availableAddressTypes: List<Any?>,

  @JvmField @SerializedName("LeadSourceName")
  val leadSourceName: Any,

  @JvmField @SerializedName("leadFieldSectionModels")
  val leadFieldSectionModels: List<Any?>,

  @JvmField @SerializedName("ProfileID")
  val profileID: Any,

  @JvmField @SerializedName("DedupeModel")
  val dedupeModel: DedupeModel,

  @JvmField @SerializedName("LoginId")
  val loginId: Any,

  @JvmField @SerializedName("ApprovalMessage")
  val approvalMessage: Any,

  @JvmField @SerializedName("Comments")
  val comments: String,

  @JvmField @SerializedName("SourceID")
  val sourceID: Int,

  @JvmField @SerializedName("RefereeCode")
  val refereeCode: Any,

  @JvmField @SerializedName("SourceName")
  val sourceName: Any,

  @JvmField @SerializedName("LeadSourceID")
  val leadSourceID: Int,

  @JvmField @SerializedName("ContactID")
  val contactID: Int,

  @JvmField @SerializedName("LeadSourceDetailsID")
  val leadSourceDetailsID: Int,

  @JvmField @SerializedName("AMCCampaignID")
  val aMCCampaignID: Any,

  @JvmField @SerializedName("IsAllowed")
  val isAllowed: Boolean,

  @JvmField @SerializedName("IsPotentialPMSProductValue")
  val isPotentialPMSProductValue: Any,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean,

  @JvmField @SerializedName("IsReCycleTransfer")
  val isReCycleTransfer: Any,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any,

  @JvmField @SerializedName("LeadID")
  val leadID: Int,

  @JvmField @SerializedName("IsPotentialPMSProduct")
  val isPotentialPMSProduct: Any,

  @JvmField @SerializedName("leadProfileParameters")
  val leadProfileParameters: Any,

  @JvmField @SerializedName("LeadCategoryID")
  val leadCategoryID: Int,

  @JvmField @SerializedName("leadFieldValueModels")
  val leadFieldValueModels: List<Any?>
)