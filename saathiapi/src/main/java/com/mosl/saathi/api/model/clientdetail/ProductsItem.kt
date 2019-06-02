package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class ProductsItem(
  @JvmField @SerializedName("CreationDate") val creationDate: String?,
  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: String?,
  @JvmField @SerializedName("Processtype") val processtype: Int,
  @JvmField @SerializedName("AdvisoryType") val advisoryType: String?,
  @JvmField @SerializedName("Product") val product: Product?,
  @JvmField @SerializedName("ProductId") val productId: Int,
  @JvmField @SerializedName("UCID") val uCID: Int,
  @JvmField @SerializedName("ClientProductMappingID") val clientProductMappingID: Int,
  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,
  @JvmField @SerializedName("SubBrokerCode") val subBrokerCode: String?,
  @JvmField @SerializedName("UnsuccessfullRecords") val unsuccessfullRecords: Int,
  @JvmField @SerializedName("Status") val status: Int,
  @JvmField @SerializedName("CreatedBy") val createdBy: String?,
  @JvmField @SerializedName("AdvisoryTypeBranchCode") val advisoryTypeBranchCode: String?,
  @JvmField @SerializedName("ProductCode") val productCode: String?,
  @JvmField @SerializedName("BatchNumber") val batchNumber: Int,
  @JvmField @SerializedName("Channel") val channel: String?,
  @JvmField @SerializedName("Advisors") val advisors: List<AdvisorsItem>?,
  @JvmField @SerializedName("CustomerId") val customerId: Int,
  @JvmField @SerializedName("InApproval") val inApproval: Boolean,
  @JvmField @SerializedName("HoldingNumber") val holdingNumber: String?,
  @JvmField @SerializedName("CustomerClassification") val customerClassification: String?,
  @JvmField @SerializedName("AdvisoryType_ProcessType") val advisoryTypeProcessType: String?,
  @JvmField @SerializedName("ParentProductID") val parentProductID: Int,
  @JvmField @SerializedName("Productcategory") val productcategory: String?,
  @JvmField @SerializedName("FamilyCode") val familyCode: String?,
  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: String?,
  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: String?,
  @JvmField @SerializedName("BranchCode") val branchCode: String
)