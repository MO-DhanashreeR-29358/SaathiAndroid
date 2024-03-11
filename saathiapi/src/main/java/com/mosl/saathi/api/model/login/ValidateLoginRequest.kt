package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class ValidateLoginRequest(

	@SerializedName("ipaddress")
	@JvmField val ipaddress: String? = null,

	@SerializedName("IsPersistance")
	@JvmField val isPersistance: Boolean? = null,

	@SerializedName("EmployeeCode")
	@JvmField val employeeCode: String? = null,

	@SerializedName("Macaddress")
	@JvmField val macaddress: String? = null,

	@SerializedName("AppId")
	@JvmField val appId: Int? = null,

	@SerializedName("clientinfo")
	@JvmField val clientinfo: String? = null,

	@SerializedName("Password")
	@JvmField val password: String? = null
)
