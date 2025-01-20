package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName



data class IndirectIFADashboardFilterResponse(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("lstProductParam")
	val lstProductParam: List<LstProductParamItem?>? = null,

	@JvmField @SerializedName("lstStageCode")
	val lstStageCode: List<LstStageCodeItem?>? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@JvmField @SerializedName("lstProduct")
	val lstProduct: List<LstProductItem?>? = null,

	@JvmField @SerializedName("lstParameter")
	val lstParameter: List<LstParameterItem?>? = null,

	@JvmField @SerializedName("lstPeriod")
	val lstPeriod: List<LstPeriodItem?>? = null
)

data class LstProductParamItem(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("DisplayText")
	val displayText: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("Value")
	val value: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)

data class LstStageCodeItem(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("DisplayText")
	val displayText: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("Value")
	val value: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)

data class LstProductItem(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("DisplayText")
	val displayText: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("Value")
	val value: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)

data class LstPeriodItem(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("DisplayText")
	val displayText: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("Value")
	val value: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)

data class LstParameterItem(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("DisplayText")
	val displayText: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("Value")
	val value: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)
