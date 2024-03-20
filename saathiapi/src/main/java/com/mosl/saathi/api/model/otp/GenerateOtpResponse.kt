package com.mosl.saathi.api.model.otp

import com.google.gson.annotations.SerializedName

data class GenerateOtpResponse(

	@SerializedName("EventListRequired")
	@JvmField val eventListRequired: Int? = null,

	@SerializedName("IsUseUrl")
	@JvmField val isUseUrl: Int? = null,

	@SerializedName("IsEKYCRevamp")
	@JvmField val isEKYCRevamp: Int? = null,

	@SerializedName("IsChequeApprovalEnable")
	@JvmField val isChequeApprovalEnable: Int? = null,

	@SerializedName("User")
	@JvmField val user: Any? = null,

	@SerializedName("IsPrimeUser")
	@JvmField val isPrimeUser: Int? = null,

	@SerializedName("IsBank")
	@JvmField val isBank: Int? = null,

	@SerializedName("IsEkycEnable")
	@JvmField val isEkycEnable: Int? = null,

	@SerializedName("IsAUShadowAccountEnable")
	@JvmField val isAUShadowAccountEnable: Int? = null,

	@SerializedName("SMTEnabled")
	@JvmField val sMTEnabled: Int? = null,

	@SerializedName("IsRetriveOTPEnable")
	@JvmField val isRetriveOTPEnable: Int? = null,

	@SerializedName("IsQRDownload")
	@JvmField val isQRDownload: Int? = null,

	@SerializedName("AuthState")
	@JvmField val authState: Int? = null,

	@SerializedName("TwoWayAuth")
	@JvmField val twoWayAuth: Int? = null
)
