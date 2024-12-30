package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName

data class IndirectIFAWidgetCommonResponse(

	@field:SerializedName("Errors")
	val errors: Any? = null,

	@field:SerializedName("Total")
	val total: Int? = null,

	@field:SerializedName("ObjectData")
	val objectData: Any? = null,

	@field:SerializedName("Permissions")
	val permissions: Permissions? = null,

	@field:SerializedName("Data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("ExtraData")
	val extraData: Any? = null
)

data class Permissions(

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

data class DataItem(

	@field:SerializedName("Birthday_Anniversary")
	val birthdayAnniversary: String? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("IFA_Name")
	val iFAName: String? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@field:SerializedName("Mobile_No")
	val mobileNo: String? = null,

	@field:SerializedName("AM_Name")
	val aMName: String? = null,

	@field:SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@field:SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@field:SerializedName("Profile")
	val profile: String? = null
)