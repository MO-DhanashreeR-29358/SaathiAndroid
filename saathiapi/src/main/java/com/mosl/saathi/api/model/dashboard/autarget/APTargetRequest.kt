package com.mosl.saathi.api.model.dashboard.autarget

import com.google.gson.annotations.SerializedName

data class APTargetRequest(
    @JvmField @SerializedName("flt_bankname") val fltBankName: String?,
    @JvmField @SerializedName("flt_bankbranchcode") val fltBankBranchCode: String?,
    @JvmField @SerializedName("flt_banksubbrokercode") val fltBankSubBrokerCode: String?,
    @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String?,
    @JvmField @SerializedName("flt_period") val fltPeriod: String?
)