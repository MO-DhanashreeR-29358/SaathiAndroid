package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationActualMarginRequest(

	/*@SerializedName("EmployeeCode")
	val employeeCode: String? = null,*/

	@SerializedName("BusinessUnitId")
	@JvmField val businessUnitId: String? = null,

	@SerializedName("ClientCode")
	@JvmField val clientCode: String? = null,

	@SerializedName("ActualActivationDate")
	@JvmField val actualActivationDate: String? = null
)
