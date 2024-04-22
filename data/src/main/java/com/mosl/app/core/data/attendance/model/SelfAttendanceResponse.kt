package com.mosl.app.core.data.attendance.model

import com.google.gson.annotations.SerializedName

data class SelfAttendanceResponse(
    @SerializedName("Present")
    val present: Float,

    @SerializedName("Absent")
    val absent: Float,

    @SerializedName("Outdoor")
    val outDoor: Float,

    @SerializedName("Holiday")
    val holiday: Float,

    @SerializedName("LeaveTaken")
    val leaveTaken: Float,

    @SerializedName("LateIn")
    val lateIn: Float,

    @SerializedName("PENDINGLEAVE")
    val pendingLeave: Float,

    @SerializedName("BALANCELEAVE")
    val balanceLeave: Float
)