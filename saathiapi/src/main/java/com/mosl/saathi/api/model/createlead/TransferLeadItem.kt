package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class TransferLeadItem(

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("IsPaymentDone") val isPaymentDone: Boolean,

  @JvmField @SerializedName("Email") val email: Any,

  @JvmField @SerializedName("CurrentUser") val currentUser: String,

  @JvmField @SerializedName("ProductName") val productName: Any,

  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,

  @JvmField @SerializedName("CheckRemark") val checkRemark: Any,

  @JvmField @SerializedName("ProductId") val productId: Int,

  @JvmField @SerializedName("DedupeWith") val dedupeWith: Any,

  @JvmField @SerializedName("Mobile") val mobile: String,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("ClientName")
  val clientName: Any,

  @JvmField @SerializedName("Remarks") val remarks: Any,

  @JvmField @SerializedName("IsTransfer") val isTransfer: Int,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("ClientCode") val clientCode: Any,

  @JvmField @SerializedName("Stage") val stage: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("LeadID") val leadID: Int,

  @JvmField @SerializedName("LeadName") val leadName: Any,

  @JvmField @SerializedName("BranchCode") val branchCode: Any,

  @JvmField @SerializedName("CaughtInC2") val caughtInC2: Int,

  @JvmField @SerializedName("ProspectId") val prospectId: Int
)