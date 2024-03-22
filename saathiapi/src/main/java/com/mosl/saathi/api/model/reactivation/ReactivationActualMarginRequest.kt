package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationActualMarginRequest(

	/*@SerializedName("EmployeeCode")
	val employeeCode: String? = null,*/

	@SerializedName("BusinessUnitId")
	val businessUnitId: String? = null,

	@SerializedName("ClientCode")
	val clientCode: String? = null,

	@SerializedName("ActualActivationDate")
	val actualActivationDate: String? = null
)
