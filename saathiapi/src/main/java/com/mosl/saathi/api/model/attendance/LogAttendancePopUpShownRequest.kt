package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class LogAttendancePopUpShownRequest(
    @SerializedName("EmployeeCode")
    @JvmField val employeeCode: String
)
