package com.mosl.saathi.api.model.saathiIndirect.SetAppointment

import com.google.gson.annotations.SerializedName

data class LookUpResponse(

	@field:SerializedName("Response")
	val response: List<ResponseItem?>? = null,

	@field:SerializedName("Status")
	val status: Boolean? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Erros")
	val erros: List<Any?>? = null
)

data class ResponseItem(

	@field:SerializedName("ParentId")
	val parentId: Int? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("SysName")
	val sysName: Any? = null,

	@field:SerializedName("Active")
	val active: Boolean? = null,

	@field:SerializedName("LookupType")
	val lookupType: Int? = null,

	@field:SerializedName("LookUpid")
	val lookUpid: Int? = null,

	@field:SerializedName("LookUpName")
	val lookUpName: String? = null,

	@field:SerializedName("BusinessUnitId")
	val businessUnitId: Int? = null,

	@field:SerializedName("Sequence")
	val sequence: Int? = null,

	@field:SerializedName("IgnoreBusinessUnit")
	val ignoreBusinessUnit: Boolean? = null,

	@field:SerializedName("LookupTypeId")
	val lookupTypeId: Int? = null
)
