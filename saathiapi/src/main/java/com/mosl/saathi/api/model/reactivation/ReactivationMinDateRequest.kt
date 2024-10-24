package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationMinDateRequest(

	@SerializedName("RequestorRMCode")
	@JvmField val requestorRMCode: String? = null
)
