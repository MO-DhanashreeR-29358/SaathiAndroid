package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class DeleteLeadCrossSellingProductItem(

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("ThirdRM")
  @JvmField val thirdRM: Any,

  @SerializedName("ProductName")
  @JvmField val productName: Any,

  @SerializedName("CrossSellingProducts")
  @JvmField val crossSellingProducts: List<Any?>,

  @SerializedName("UCID")
  @JvmField val uCID: Int,

  @SerializedName("IsProductDisabled")
  @JvmField val isProductDisabled: Int,

  @SerializedName("FollowUpDate")
  @JvmField val followUpDate: Any,

  @SerializedName("FourthRMAssignDate")
  @JvmField val fourthRMAssignDate: Any,

  @SerializedName("PendingDays")
  @JvmField val pendingDays: Int,

  @SerializedName("ThirdRMAssignDate")
  @JvmField val thirdRMAssignDate: Any,

  @SerializedName("FourthRMAssignedBy")
  @JvmField val fourthRMAssignedBy: Any,

  @SerializedName("IsProductSpecialistEditDisabled")
  @JvmField val isProductSpecialistEditDisabled: Int,

  @SerializedName("SecondaryRM")
  @JvmField val secondaryRM: Any,

  @SerializedName("AssignedByDate")
  @JvmField val assignedByDate: String,

  @SerializedName("StatusID")
  @JvmField val statusID: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("AssignedBy")
  @JvmField val assignedBy: String,

  @SerializedName("IsDefault")
  @JvmField val isDefault: Boolean,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("StageName")
  @JvmField val stageName: Any,

  @SerializedName("StageID")
  @JvmField val stageID: Int,

  @SerializedName("LowMargin")
  @JvmField val lowMargin: Any,

  @SerializedName("PrimaryRM_Name")
  @JvmField val primaryRMName: Any,

  @SerializedName("ProductID")
  @JvmField val productID: Int,

  @SerializedName("StatusName")
  @JvmField val statusName: Any,

  @SerializedName("ProductSpecialist_Name")
  @JvmField val productSpecialistName: Any,

  @SerializedName("lead")
  @JvmField val lead: Any,

  @SerializedName("InterestedIn")
  @JvmField val interestedIn: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("SecondaryRMAssignedDate")
  @JvmField val secondaryRMAssignedDate: Any,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("ClientCode")
  @JvmField val clientCode: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("SecondaryRMAssignedBy")
  @JvmField val secondaryRMAssignedBy: Any,

  @SerializedName("LeadID")
  @JvmField val leadID: Int,

  @SerializedName("LeadProductMappingID")
  @JvmField val leadProductMappingID: Int,

  @SerializedName("IsPotentialPMSProduct")
  @JvmField val isPotentialPMSProduct: Any,

  @SerializedName("FourthRM")
  @JvmField val fourthRM: Any,

  @SerializedName("ProductSpecialist")
  @JvmField val productSpecialist: String,

  @SerializedName("ThirdRMAssignedBy")
  @JvmField val thirdRMAssignedBy: Any,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: String,

  @SerializedName("IsCrossSellingProduct")
  @JvmField val isCrossSellingProduct: Boolean,

  @SerializedName("Is_LowMargin_Lead")
  @JvmField val isLowMarginLead: Any
)