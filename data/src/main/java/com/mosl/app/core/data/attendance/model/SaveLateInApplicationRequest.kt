package com.mosl.app.core.data.attendance.model

import com.google.gson.annotations.SerializedName

data class SaveLateInApplicationRequest(
    @SerializedName("EmployeeID")
    val employeeId: String,

    @SerializedName("RegularizationDate")
    val regularizationDate: String,

    @SerializedName("LateInReason")
    val lateInReason: String,

    @SerializedName("remark")
    val remark: String
)