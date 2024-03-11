package com.mosl.mobile.user.model

import com.google.gson.annotations.SerializedName

data class ValidateOtpPostModel(
    @JvmField val  ipaddress: String? = null,

    @JvmField val  isPersistance: Boolean? = null,

    @JvmField val  employeeCode: String? = null,

    @JvmField val  macaddress: String? = null,

    @JvmField val  appId: Int? = null,

    @JvmField val  clientinfo: String? = null,

    @JvmField val  oTP: String? = null,

    @JvmField val  password: String? = null
)
