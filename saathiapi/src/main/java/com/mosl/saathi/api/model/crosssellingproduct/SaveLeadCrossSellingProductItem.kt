package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class SaveLeadCrossSellingProductItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("LeadCrossSellingProductId")
  @JvmField val leadCrossSellingProductId: Int,

  @SerializedName("ProductName")
  @JvmField val productName: Any,

  @SerializedName("CrossSellingProductName")
  @JvmField val crossSellingProductName: Any,

  @SerializedName("CrossSellingStatusName")
  @JvmField val crossSellingStatusName: Any,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("CrossSellingProductId")
  @JvmField val crossSellingProductId: Int,

  @SerializedName("InterestedInId")
  @JvmField val interestedInId: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("LeadId")
  @JvmField val leadId: Int,

  @SerializedName("CrossSellingStatusId")
  @JvmField val crossSellingStatusId: Int
)