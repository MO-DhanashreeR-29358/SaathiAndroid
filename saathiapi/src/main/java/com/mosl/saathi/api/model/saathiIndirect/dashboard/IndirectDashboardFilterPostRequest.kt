package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName

data class IndirectDashboardFilterPostRequest(
    @JvmField @SerializedName("EmployeeCode")
    val employeeCode: String? = null
)
