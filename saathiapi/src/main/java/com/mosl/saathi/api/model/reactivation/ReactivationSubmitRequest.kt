package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationSubmitRequest(

	@field:SerializedName("TGS_Edumo_Revenue")
	val tGSEdumoRevenue: String? = null,

	@field:SerializedName("Remarks")
	val remarks: String? = null,

	@field:SerializedName("MarginProofFileName")
	val marginProofFileName: String? = null,

	@field:SerializedName("ClientCode")
	val clientCode: String? = null,

	@field:SerializedName("RequestTypeFlag")
	val requestTypeFlag: String? = null,

	@field:SerializedName("ActualActivationDate")
	val actualActivationDate: String? = null,

	@field:SerializedName("DocumentName")
	val documentName: String? = null,

	@field:SerializedName("TotalMarginCollected")
	val totalMarginCollected: String? = null,

	@field:SerializedName("Base64FileString")
	val base64FileString: String? = null
)
