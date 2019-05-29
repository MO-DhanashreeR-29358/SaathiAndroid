package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class ProductsItem(

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("Processtype")
  @JvmField val processtype: Int,

  @SerializedName("AdvisoryType")
  @JvmField val advisoryType: String,

  @SerializedName("Product")
  @JvmField val product: Product,

  @SerializedName("ProductId")
  @JvmField val productId: Int,

  @SerializedName("UCID")
  @JvmField val uCID: Int,

  @SerializedName("ClientProductMappingID")
  @JvmField val clientProductMappingID: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("SubBrokerCode")
  @JvmField val subBrokerCode: String,

  @SerializedName("UnsuccessfullRecords")
  @JvmField val unsuccessfullRecords: Int,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: Any,

  @SerializedName("AdvisoryTypeBranchCode")
  @JvmField val advisoryTypeBranchCode: String,

  @SerializedName("ProductCode")
  @JvmField val productCode: Any,

  @SerializedName("BatchNumber")
  @JvmField val batchNumber: Int,

  @SerializedName("Channel")
  @JvmField val channel: String,

  @SerializedName("Advisors")
  @JvmField val advisors: List<AdvisorsItem?>,

  @SerializedName("CustomerId")
  @JvmField val customerId: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("HoldingNumber")
  @JvmField val holdingNumber: String,

  @SerializedName("CustomerClassification")
  @JvmField val customerClassification: String,

  @SerializedName("AdvisoryType_ProcessType")
  @JvmField val advisoryTypeProcessType: Any,

  @SerializedName("ParentProductID")
  @JvmField val parentProductID: Int,

  @SerializedName("Productcategory")
  @JvmField val productcategory: String,

  @SerializedName("FamilyCode")
  @JvmField val familyCode: String,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String
)