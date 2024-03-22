package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationClientSearchResponse(

	@SerializedName("CreationDate")
	@JvmField val  creationDate: Any? = null,

	@SerializedName("LastModifiedBy")
	@JvmField val  lastModifiedBy: Any? = null,

	@SerializedName("FinalActivationDate_365")
	@JvmField val  finalActivationDate365: String? = null,

	@SerializedName("ValuePack_Last_15Days")
	@JvmField val  valuePackLast15Days: String? = null,

	@SerializedName("FinalSegment")
	@JvmField val  finalSegment: String? = null,

	@SerializedName("FinalActivationDate")
	@JvmField val  finalActivationDate: String? = null,

	@SerializedName("CurrentRMCode")
	@JvmField val  currentRMCode: String? = null,

	@SerializedName("MetInLast30Days")
	@JvmField val  metInLast30Days: String? = null,

	@SerializedName("ClientVerticalSameAsRMVertical")
	@JvmField val  clientVerticalSameAsRMVertical: String? = null,

	@SerializedName("Margin_Last_15Days")
	@JvmField val  marginLast15Days: String? = null,

	@SerializedName("StartDate")
	@JvmField val  startDate: String? = null,

	@SerializedName("ClientName")
	@JvmField val  clientName: String? = null,

	@SerializedName("TGS_Edumo_Revenue")
	@JvmField val  tGSEdumoRevenue: Any? = null,

	@SerializedName("SubSegment")
	@JvmField val  subSegment: String? = null,

	@SerializedName("CurrentRMName")
	@JvmField val  currentRMName: String? = null,

	@SerializedName("Remarks")
	@JvmField val  remarks: Any? = null,

	@SerializedName("Branch")
	@JvmField val  branch: String? = null,

	@SerializedName("RequestTypeFlag")
	@JvmField val  requestTypeFlag: Any? = null,

	@SerializedName("LastTradedDate_180")
	@JvmField val  lastTradedDate180: String? = null,

	@SerializedName("LastTradedDate")
	@JvmField val  lastTradedDate: String? = null,

	@SerializedName("TotalMarginCollected")
	@JvmField val  totalMarginCollected: Any? = null,

	@SerializedName("ActualActivationDate")
	@JvmField val  actualActivationDate: Any? = null,

	@SerializedName("LastMarginDate")
	@JvmField val  lastMarginDate: String? = null,

	@SerializedName("RMVertical")
	@JvmField val  rMVertical: String? = null,

	@SerializedName("CreatedBy")
	@JvmField val  createdBy: Any? = null,

	@SerializedName("MappedAdvisorCode")
	@JvmField val  mappedAdvisorCode: String? = null,

	@SerializedName("Zone")
	@JvmField val  zone: String? = null,

	@SerializedName("UnutilizedVP")
	@JvmField val  unutilizedVP: String? = null,

	@SerializedName("StockMarginCollected")
	@JvmField val  stockMarginCollected: Any? = null,

	@SerializedName("RequestorRMCode")
	@JvmField val  requestorRMCode: Any? = null,

	@SerializedName("MarginProofFileName")
	@JvmField val  marginProofFileName: Any? = null,

	@SerializedName("BusinessUnitId")
	@JvmField val  businessUnitId: Any? = null,

	@SerializedName("ReactivationType")
	@JvmField val  reactivationType: Int? = null,

	@SerializedName("MinMargin_VP")
	@JvmField val  minMarginVP: String? = null,

	@SerializedName("MappedAdvisorVertical")
	@JvmField val  mappedAdvisorVertical: String? = null,

	@SerializedName("CashMarginCollected")
	@JvmField val  cashMarginCollected: Any? = null,

	@SerializedName("LedgerBalance")
	@JvmField val  ledgerBalance: String? = null,

	@SerializedName("RequestTypeDesc")
	@JvmField val  requestTypeDesc: Any? = null,

	@SerializedName("ClientCode")
	@JvmField val  clientCode: String? = null,

	@SerializedName("LastModificationDate")
	@JvmField val  lastModificationDate: Any? = null,

	@SerializedName("Region")
	@JvmField val  region: String? = null,

	@SerializedName("Id")
	@JvmField val  id: Int? = null,

	@SerializedName("MappedAdvisorName")
	@JvmField val  mappedAdvisorName: String? = null,

	@SerializedName("DPBalance")
	@JvmField val  dPBalance: String? = null,

	@SerializedName("ErrorMessage")
	@JvmField val  errorMessage: Any? = null,

	@SerializedName("BranchShift")
	@JvmField val  branchShift: Any? = null
)
