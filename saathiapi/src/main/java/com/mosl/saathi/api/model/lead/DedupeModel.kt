package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class DedupeModel(

  @field:SerializedName("Status")
  val status: Int? = null,

  @field:SerializedName("IsPaymentDone")
  val isPaymentDone: Boolean? = null,

  @field:SerializedName("Email")
  val email: Any? = null,

  @field:SerializedName("CurrentUser")
  val currentUser: Any? = null,

  @field:SerializedName("ProductName")
  val productName: Any? = null,

  @field:SerializedName("BusinessUnitId")
  val businessUnitId: Int? = null,

  @field:SerializedName("CheckRemark")
  val checkRemark: Any? = null,

  @field:SerializedName("ProductId")
  val productId: Int? = null,

  @field:SerializedName("DedupeWith")
  val dedupeWith: Any? = null,

  @field:SerializedName("Mobile")
  val mobile: Any? = null,

  @field:SerializedName("InApproval")
  val inApproval: Boolean? = null,

  @field:SerializedName("ClientName")
  val clientName: Any? = null,

  @field:SerializedName("Remarks")
  val remarks: Any? = null,

  @field:SerializedName("IsTransfer")
  val isTransfer: Int? = null,

  @field:SerializedName("ApprovalFieldList")
  val approvalFieldList: Any? = null,

  @field:SerializedName("ClientCode")
  val clientCode: Any? = null,

  @field:SerializedName("Stage")
  val stage: Any? = null,

  @field:SerializedName("IsAdminMode")
  val isAdminMode: Boolean? = null,

  @field:SerializedName("LeadID")
  val leadID: Int? = null,

  @field:SerializedName("LeadName")
  val leadName: Any? = null,

  @field:SerializedName("BranchCode")
  val branchCode: Any? = null,

  @field:SerializedName("CaughtInC2")
  val caughtInC2: Int? = null,

  @field:SerializedName("ProspectId")
  val prospectId: Int? = null
)