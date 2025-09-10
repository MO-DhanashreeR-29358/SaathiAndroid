package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName



data class IndirectCCDashboardFilterResponse(

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,


	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@JvmField @SerializedName("lstAdvisor")
	val lstAdvisor: List<LstCCAdvisorItem?>? = null,

	@JvmField @SerializedName("lstPeriod")
	val lstPeriod: List<LstCCPeriodItem?>? = null
)

data class LstCCPeriodItem(

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

data class LstCCAdvisorItem(

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