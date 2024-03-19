package com.mosl.mobile.user.model

import com.google.gson.annotations.SerializedName

data class GenerateOtpResponseModel(


    @JvmField val eventListRequired: Int? = null,


    @JvmField val isUseUrl: Int? = null,


    @JvmField val isEKYCRevamp: Int? = null,


    @JvmField val isChequeApprovalEnable: Int? = null,


    @JvmField val user: Any? = null,


    @JvmField val isPrimeUser: Int? = null,


    @JvmField val isBank: Int? = null,


    @JvmField val isEkycEnable: Int? = null,


    @JvmField val isAUShadowAccountEnable: Int? = null,

    
    @JvmField val sMTEnabled: Int? = null,


    @JvmField val isRetriveOTPEnable: Int? = null,


    @JvmField val isQRDownload: Int? = null,


    @JvmField val authState: Int? = null,


    @JvmField val twoWayAuth: Int? = null
)
