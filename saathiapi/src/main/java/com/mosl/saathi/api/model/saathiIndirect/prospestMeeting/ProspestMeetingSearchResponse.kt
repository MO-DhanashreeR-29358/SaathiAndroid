package com.mosl.saathi.api.model.saathiIndirect.prospestMeeting

import com.google.gson.annotations.SerializedName

data class ProspestMeetingSearchResponse(

	@field:SerializedName("Response")
	val response: List<ResponseItem?>? = null,

	@field:SerializedName("Status")
	val status: Boolean? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Erros")
	val erros: List<Any?>? = null
)

data class ResponseItem(

	@field:SerializedName("FranchiseeCode")
	val franchiseeCode: String? = null,

	@field:SerializedName("AdvisoryType")
	val advisoryType: Any? = null,

	@field:SerializedName("BusinessProfileId")
	val businessProfileId: Int? = null,

	@field:SerializedName("RegionCode")
	val regionCode: Any? = null,

	@field:SerializedName("ProductName")
	val productName: Any? = null,

	@field:SerializedName("ProductId")
	val productId: Int? = null,

	@field:SerializedName("EquityTraderCode")
	val equityTraderCode: Any? = null,

	@field:SerializedName("BusinessChannelId")
	val businessChannelId: Int? = null,

	@field:SerializedName("EmployeeCodes")
	val employeeCodes: Any? = null,

	@field:SerializedName("clientType")
	val clientType: Any? = null,

	@field:SerializedName("LookUpName")
	val lookUpName: Any? = null,

	@field:SerializedName("LeadId")
	val leadId: Int? = null,

	@field:SerializedName("SubBrokerCode")
	val subBrokerCode: Any? = null,

	@field:SerializedName("BusinessUnitName")
	val businessUnitName: Any? = null,

	@field:SerializedName("ProfileGroup")
	val profileGroup: Any? = null,

	@field:SerializedName("IFANAME")
	val iFANAME: Any? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("IsDefault")
	val isDefault: Boolean? = null,

	@field:SerializedName("EmployeeCode")
	val employeeCode: Any? = null,

	@field:SerializedName("FirstName")
	val firstName: Any? = null,

	@field:SerializedName("BusinessUnitId")
	val businessUnitId: Int? = null,

	@field:SerializedName("ClientCODE")
	val clientCODE: String? = null,

	@field:SerializedName("TraderCode")
	val traderCode: Any? = null,

	@field:SerializedName("NumberofClientMapped")
	val numberofClientMapped: Int? = null,

	@field:SerializedName("IsPrimaryadvisor")
	val isPrimaryadvisor: Boolean? = null,

	@field:SerializedName("ClientNAME")
	val clientNAME: Any? = null,

	@field:SerializedName("ChannelName")
	val channelName: Any? = null,

	@field:SerializedName("TotalClientMapped")
	val totalClientMapped: Int? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("BusinessUnit")
	val businessUnit: Any? = null,

	@field:SerializedName("AreaCode")
	val areaCode: Any? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("IFACODE")
	val iFACODE: Any? = null,

	@field:SerializedName("EmployeeName")
	val employeeName: Any? = null,

	@field:SerializedName("UserType")
	val userType: Int? = null,

	@field:SerializedName("BranchCode")
	val branchCode: Any? = null,

	@field:SerializedName("CommodityTraderCode")
	val commodityTraderCode: Any? = null,

	@field:SerializedName("PrimaryRM")
	val primaryRM: Any? = null,

	@field:SerializedName("BusinessProfileName")
	val businessProfileName: Any? = null
)
