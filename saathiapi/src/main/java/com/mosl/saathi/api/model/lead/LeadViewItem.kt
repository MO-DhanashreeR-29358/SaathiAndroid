package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadViewItem(

  @JvmField @SerializedName("CreatedThrough") val createdThrough: String,

  @JvmField @SerializedName("CreationDate") val creationDate: String,

  @JvmField @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String,

  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,

  @JvmField @SerializedName("ContactPerson") val contactPerson: Any,

  @JvmField @SerializedName("LeadOwner") val leadOwner: String?,

  @JvmField @SerializedName("ProspectSource") val prospectSource: String,

  @JvmField @SerializedName("PotentialPMSProductList") val potentialPMSProductList: Any,

  @JvmField @SerializedName("contact") val contact: Contact?,

  @JvmField @SerializedName(
      "leadProductMapping"
  ) val leadProductMapping: List<LeadProductMappingItem>,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("IsPotentialPMSProductText") val isPotentialPMSProductText: Any,

  @JvmField @SerializedName(
      "StageWiseFieldConfiguration"
  ) val stageWiseFieldConfiguration: List<StageWiseFieldConfigurationItem?>,

  @JvmField @SerializedName("LastReCycleTransferDate") val lastReCycleTransferDate: Any,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("CreatedBy") val createdBy: String,

  @JvmField @SerializedName("RefereeEmail") val refereeEmail: Any,

  @JvmField @SerializedName("OAOUserid") val oAOUserid: Int = 0,

  @JvmField @SerializedName("QueryId") val queryId: Int,

  @JvmField @SerializedName("ProspectRefereEmail") val prospectRefereEmail: String,

  @JvmField @SerializedName("ChannelID") val channelID: Any,

  @JvmField @SerializedName("IsProductSpecialist") val isProductSpecialist: String,

  @JvmField @SerializedName("IsAadhar") val isAadhar: Int,

  @JvmField @SerializedName("LeadCategoryText") val leadCategoryText: String,

  @JvmField @SerializedName("Last_Recycle_Date") val lastRecycleDate: Any,

  @JvmField @SerializedName("Branchcode") val branchcode: String?,

  @JvmField @SerializedName("Active") val active: Boolean,

  @JvmField @SerializedName("Profession") val profession: String,

  @JvmField @SerializedName("ProspectRefereName") val prospectRefereName: String,

  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: String,

  @JvmField @SerializedName("ProfileParamPercentage") val profileParamPercentage: Int,

  @JvmField @SerializedName("CanEditLead") val canEditLead: Int,

  @JvmField @SerializedName("Is_Recycle_Lead") val isRecycleLead: Any,

  @JvmField @SerializedName("RefereeMobile") val refereeMobile: Any,

  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: String,

  @JvmField @SerializedName("RefereeName") val refereeName: Any,

  @JvmField @SerializedName("ProfessionID") val professionID: Any,

  @JvmField @SerializedName("CampaignName") val campaignName: Any,

  @JvmField @SerializedName("Profile") val profile: Any,

  @JvmField @SerializedName("LeadSourceDetails") val leadSourceDetails: Any,

  @JvmField @SerializedName("MoneyControlPortfolioURL") val moneyControlPortfolioURL: String,

  @JvmField @SerializedName("AvailableAddressTypes") val availableAddressTypes: List<Any?>,

  @JvmField @SerializedName("LeadSourceName") val leadSourceName: Any,

  @JvmField @SerializedName("leadFieldSectionModels") val leadFieldSectionModels: List<Any?>,

  @JvmField @SerializedName("ProfileID") val profileID: Int,

  @JvmField @SerializedName("DedupeModel") val dedupeModel: DedupeModel,

  @JvmField @SerializedName("LoginId") val loginId: Any,

  @JvmField @SerializedName("ApprovalMessage") val approvalMessage: Any,

  @JvmField @SerializedName("Comments") val comments: String,

  @JvmField @SerializedName("SourceID") val sourceID: Int,

  @JvmField @SerializedName("RefereeCode") val refereeCode: Any,

  @JvmField @SerializedName("SourceName") val sourceName: String?,

  @JvmField @SerializedName("LeadSourceID") val leadSourceID: Int,

  @JvmField @SerializedName("ContactID") val contactID: Int,

  @JvmField @SerializedName("LeadSourceDetailsID") val leadSourceDetailsID: String?,

  @JvmField @SerializedName("AMCCampaignID") val aMCCampaignID: Int,

  @JvmField @SerializedName("IsAllowed") val isAllowed: Boolean,

  @JvmField @SerializedName("IsPotentialPMSProductValue") val isPotentialPMSProductValue: String,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("IsReCycleTransfer") val isReCycleTransfer: Any,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("LeadID") val leadID: String?,

  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: String,

  @JvmField @SerializedName("leadProfileParameters") val leadProfileParameters: Any,

  @JvmField @SerializedName("LeadCategoryID") val leadCategoryID: Int,

  @JvmField @SerializedName("leadFieldValueModels") val leadFieldValueModels: List<Any?>
)