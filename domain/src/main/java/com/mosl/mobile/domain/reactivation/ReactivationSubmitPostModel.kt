package com.mosl.mobile.domain.reactivation

data class ReactivationSubmitPostModel(

    @JvmField val tGSEdumoRevenue: String,

    @JvmField val remarks: String,

    @JvmField val marginProofFileName: String,

    @JvmField val clientCode: String,

    @JvmField val requestTypeFlag: String,

    @JvmField val actualActivationDate: String,

    @JvmField val documentName: String,

    @JvmField val totalMarginCollected: String,

    @JvmField val base64FileString: String?=null,

    @JvmField val uriString:String,
    @JvmField var isSearched:Boolean
)
