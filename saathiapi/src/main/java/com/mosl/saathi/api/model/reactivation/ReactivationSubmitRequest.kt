package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationSubmitRequest(

	@SerializedName("TGS_Edumo_Revenue")
	@JvmField val tGSEdumoRevenue: String? = null,

	@SerializedName("Remarks")
	@JvmField val remarks: String? = null,

	@SerializedName("MarginProofFileName")
	@JvmField val marginProofFileName: String? = null,

	@SerializedName("ClientCode")
	@JvmField val clientCode: String? = null,

	@SerializedName("RequestTypeFlag")
	@JvmField val requestTypeFlag: String? = null,

	@SerializedName("ActualActivationDate")
	@JvmField val actualActivationDate: String? = null,

	@SerializedName("DocumentName")
	@JvmField val documentName: String? = null,

	@SerializedName("TotalMarginCollected")
	@JvmField val totalMarginCollected: String? = null,

	@SerializedName("Base64FileString")
	@JvmField val base64FileString: String? = null
)
