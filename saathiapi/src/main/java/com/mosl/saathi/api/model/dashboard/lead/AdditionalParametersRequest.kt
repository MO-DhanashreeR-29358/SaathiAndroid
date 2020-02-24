package com.mosl.saathi.api.model.dashboard.lead

import com.google.gson.annotations.SerializedName

data class AdditionalParametersRequest(
    @JvmField @SerializedName("flt_bankname") val fltBankName: String?,
    @JvmField @SerializedName("flt_bankbranchcode") val fltBankBranchCode: String?,
    @JvmField @SerializedName("flt_banksubbrokercode") val fltBankSubBrokerCode: String?,
    @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String?,
    @JvmField @SerializedName("flt_period") val fltPeriod: String?,
    @JvmField @SerializedName("flt_product") val fltProduct: String?,
    @JvmField @SerializedName("flt_prospect") val fltProspect: String?
)