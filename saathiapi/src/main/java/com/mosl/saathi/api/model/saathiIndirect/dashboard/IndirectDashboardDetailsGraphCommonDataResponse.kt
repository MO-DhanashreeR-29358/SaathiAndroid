package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName

data class IndirectDashboardDetailsGraphCommonDataResponse(

	@field:SerializedName("Response")
	val response: IndirectDashboardDetailsGraphResponse? = null,

	@field:SerializedName("Status")
	val status: Boolean? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Erros")
	val erros: List<Any?>? = null
)

data class IndirectDashboardDetailsGraphResponse(

	@field:SerializedName("Errors")
	val errors: Any? = null,

	@field:SerializedName("Total")
	val total: Int? = null,

	@field:SerializedName("ObjectData")
	val objectData: IndirectDashboardDetailsGraphObjectData? = null,

	@field:SerializedName("Permissions")
	val permissions: IndirectDashboardDetailsGraphPermissions? = null,

	@field:SerializedName("Data")
	val data: Any? = null,

	@field:SerializedName("ExtraData")
	val extraData: Any? = null
)

data class IndirectDashboardDetailsGraphObjectData(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("graphTeamUnique")
	val graphTeamUnique: List<GraphTeamUniqueItem?>? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@field:SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@field:SerializedName("graphBranchWise")
	val graphBranchWise: List<Any?>? = null,

	@field:SerializedName("graphProductWiseSales")
	val graphProductWiseSales: List<GraphTeamUniqueItem?>? = null,

	@field:SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@field:SerializedName("graphMyUnique")
	val graphMyUnique: List<GraphTeamUniqueItem?>? = null,

	@field:SerializedName("graphTargetNotMet")
	val graphTargetNotMet: List<GraphTeamUniqueItem?>? = null
)

data class GraphTeamUniqueItem(

	@field:SerializedName("Val")
	val Val: String? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("Category")
	val category: Any? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@field:SerializedName("RevenuePercentage")
	val revenuePercentage: Any? = null,

	@field:SerializedName("Product")
	val product: String? = null,

	@field:SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@field:SerializedName("Branch")
	val branch: Any? = null,

	@field:SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("Parameter")
	val parameter: String? = null,

	@field:SerializedName("Region")
	val region: Any? = null,

	@field:SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null
)

data class IndirectDashboardDetailsGraphPermissions(

	@field:SerializedName("Read")
	val read: Boolean? = null,

	@field:SerializedName("Write")
	val write: Boolean? = null,

	@field:SerializedName("BusinessProfileId")
	val businessProfileId: Int? = null,

	@field:SerializedName("Export")
	val export: Boolean? = null,

	@field:SerializedName("SystemName")
	val systemName: Any? = null
)
