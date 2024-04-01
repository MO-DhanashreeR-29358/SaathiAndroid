package com.mosl.user.domain.model

import java.util.Date

data class User(
    @JvmField val attenCount: String,
    @JvmField val employeeId: String,
    @JvmField val employeeCode: String,
    @JvmField val employeeMoNo: String,
    @JvmField val fullName: String,
    @JvmField val gender: Int,
    @JvmField val inTime: String = "",
    @JvmField var updateDate: Date? = null,
    @JvmField val photoPath: String,
    @JvmField val token: String = "",
    @JvmField val departmentCode: String = "",
    @JvmField val uniqueId: Int,
    @JvmField val collectionOfficer: Boolean = false
)