package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationClientSearchREquest(

	@SerializedName("EmployeeCode")
	 @JvmField val employeeCode: String? = null,

	@SerializedName("ClientCode")
	 @JvmField val clientCode: String? = null,

	@SerializedName("RequestTypeFlag")
	 @JvmField val requestTypeFlag: String? = null,

	@SerializedName("BranchShift")
	 @JvmField val branchShift: String? = null
)
