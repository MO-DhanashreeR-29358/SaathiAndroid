package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadProductMappingItem(
  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: String?,
  @JvmField @SerializedName("ThirdRM") val thirdRM: String?,
  @JvmField @SerializedName("ProductName") val productName: String?,
  @JvmField @SerializedName(
      "CrossSellingProducts"
  ) val crossSellingProducts: List<CrossSellingProduct>?,
  @JvmField @SerializedName("UCID") val uCID: String?,
  @JvmField @SerializedName("IsProductDisabled") val isProductDisabled: String?,
  @JvmField @SerializedName("FollowUpDate") val followUpDate: String?,
  @JvmField @SerializedName("FourthRMAssignDate") val fourthRMAssignDate: String?,
  @JvmField @SerializedName("PendingDays") val pendingDays: Int?,
  @JvmField @SerializedName("ThirdRMAssignDate") val thirdRMAssignDate: String?,
  @JvmField @SerializedName("FourthRMAssignedBy") val fourthRMAssignedBy: String?,
  @JvmField @SerializedName(
      "IsProductSpecialistEditDisabled"
  ) val isProductSpecialistEditDisabled: String?,
  @JvmField @SerializedName("SecondaryRM") val secondaryRM: String?,
  @JvmField @SerializedName("AssignedByDate") val assignedByDate: String?,
  @JvmField @SerializedName("StatusID") val statusID: Int?,
  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean?,
  @JvmField @SerializedName("AssignedBy") val assignedBy: String?,
  @JvmField @SerializedName("IsDefault") val isDefault: Boolean,
  @JvmField @SerializedName("Status") val status: Int?,
  @JvmField @SerializedName("StageName") val stageName: String?,
  @JvmField @SerializedName("StageID") val stageID: Int?,
  @JvmField @SerializedName("LowMargin") val lowMargin: String?,
  @JvmField @SerializedName("PrimaryRM_Name") val primaryRMName: String?,
  @JvmField @SerializedName("ProductID") val productID: String?,
  @JvmField @SerializedName("StatusName") val statusName: String?,
  @JvmField @SerializedName("ProductSpecialist_Name") val productSpecialistName: String?,
  @JvmField @SerializedName("lead") val lead: String?,
  @JvmField @SerializedName("InterestedIn") val interestedIn: Int,
  @JvmField @SerializedName("InApproval") val inApproval: Boolean?,
  @JvmField @SerializedName("Active") val active: Boolean?,
  @JvmField @SerializedName("SecondaryRMAssignedDate") val secondaryRMAssignedDate: String?,
  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: String?,
  @JvmField @SerializedName("ClientCode") val clientCode: String?,
  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: String?,
  @JvmField @SerializedName("SecondaryRMAssignedBy") val secondaryRMAssignedBy: String?,
  @JvmField @SerializedName("LeadID") val leadID: Int?,
  @JvmField @SerializedName("LeadProductMappingID") val leadProductMappingID: Int?,
  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: String?,
  @JvmField @SerializedName("FourthRM") val fourthRM: String?,
  @JvmField @SerializedName("ProductSpecialist") val productSpecialist: String?,
  @JvmField @SerializedName("ThirdRMAssignedBy") val thirdRMAssignedBy: String?,
  @JvmField @SerializedName("PrimaryRM") val primaryRM: String?,
  @JvmField @SerializedName("IsCrossSellingProduct") val isCrossSellingProduct: Boolean,
  @JvmField @SerializedName("Is_LowMargin_Lead") val isLowMarginLead: String?
)

data class CrossSellingProduct(
  @JvmField @SerializedName("LeadCrossSellingProductId") val leadProductId: String?,
  @JvmField @SerializedName("CrossSellingProductId") val productId: String?,
  @JvmField @SerializedName("CrossSellingStatusId") val statusId: String?,
  @JvmField @SerializedName("CrossSellingProductName") val productName: String?,
  @JvmField @SerializedName("CrossSellingStatusName") val statusName: String?
)