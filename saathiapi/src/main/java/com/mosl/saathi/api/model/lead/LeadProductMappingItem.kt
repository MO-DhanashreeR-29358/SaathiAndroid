package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadProductMappingItem(

  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: Any,

  @JvmField @SerializedName("ThirdRM") val thirdRM: Any,

  @JvmField @SerializedName("ProductName") val productName: Any,

  @JvmField @SerializedName("CrossSellingProducts") val crossSellingProducts: List<Any?>,

  @JvmField @SerializedName("UCID") val uCID: Any,

  @JvmField @SerializedName("IsProductDisabled") val isProductDisabled: Int,

  @JvmField @SerializedName("FollowUpDate") val followUpDate: Any,

  @JvmField @SerializedName("FourthRMAssignDate") val fourthRMAssignDate: Any,

  @JvmField @SerializedName("PendingDays") val pendingDays: String?,

  @JvmField @SerializedName("ThirdRMAssignDate") val thirdRMAssignDate: Any,

  @JvmField @SerializedName("FourthRMAssignedBy") val fourthRMAssignedBy: Any,

  @JvmField @SerializedName(
      "IsProductSpecialistEditDisabled"
  ) val isProductSpecialistEditDisabled: Int,

  @JvmField @SerializedName("SecondaryRM") val secondaryRM: Any,

  @JvmField @SerializedName("AssignedByDate") val assignedByDate: String,

  @JvmField @SerializedName("StatusID") val statusID: Int,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("AssignedBy") val assignedBy: String,

  @JvmField @SerializedName("IsDefault") val isDefault: String?,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("StageName") val stageName: String?,

  @JvmField @SerializedName("StageID") val stageID: Int,

  @JvmField @SerializedName("LowMargin") val lowMargin: Any,

  @JvmField @SerializedName("PrimaryRM_Name") val primaryRMName: Any,

  @JvmField @SerializedName("ProductID") val productID: Any,

  @JvmField @SerializedName("StatusName") val statusName: Any,

  @JvmField @SerializedName("ProductSpecialist_Name") val productSpecialistName: Any,

  @JvmField @SerializedName("lead") val lead: Any,

  @JvmField @SerializedName("InterestedIn") val interestedIn: String?,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("Active") val active: Boolean,

  @JvmField @SerializedName("SecondaryRMAssignedDate") val secondaryRMAssignedDate: Any,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("ClientCode") val clientCode: String?,

  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: Any,

  @JvmField @SerializedName("SecondaryRMAssignedBy") val secondaryRMAssignedBy: Any,

  @JvmField @SerializedName("LeadID") val leadID: Int,

  @JvmField @SerializedName("LeadProductMappingID") val leadProductMappingID: Int,

  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: Any,

  @JvmField @SerializedName("FourthRM") val fourthRM: Any,

  @JvmField @SerializedName("ProductSpecialist") val productSpecialist: Any,

  @JvmField @SerializedName("ThirdRMAssignedBy") val thirdRMAssignedBy: Any,

  @JvmField @SerializedName("PrimaryRM") val primaryRM: String,

  @JvmField @SerializedName("IsCrossSellingProduct") val isCrossSellingProduct: Boolean,

  @JvmField @SerializedName("Is_LowMargin_Lead") val isLowMarginLead: Any
)