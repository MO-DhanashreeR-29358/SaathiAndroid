package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class AutoCompleteAttendeeItem(

  @JvmField @SerializedName("AdvisoryType") val advisoryType: Any,

  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: Int,

  @JvmField @SerializedName("RegionCode") val regionCode: Any,

  @JvmField @SerializedName("ProductName") val productName: Any,

  @JvmField @SerializedName("ProductId") val productId: Int,

  @JvmField @SerializedName("EquityTraderCode") val equityTraderCode: Any,

  @JvmField @SerializedName("BusinessChannelId") val businessChannelId: Int,

  @JvmField @SerializedName("EmployeeCodes") val employeeCodes: Any,

  @JvmField @SerializedName("clientType") val clientType: Any,

  @JvmField @SerializedName("LookUpName") val lookUpName: Any,

  @JvmField @SerializedName("SubBrokerCode") val subBrokerCode: Any,

  @JvmField @SerializedName("BusinessUnitName") val businessUnitName: Any,

  @JvmField @SerializedName("ProfileGroup") val profileGroup: Any,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("IsDefault") val isDefault: Boolean,

  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?,

  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,

  @JvmField @SerializedName("TraderCode") val traderCode: Any,

  @JvmField @SerializedName("NumberofClientMapped") val numberofClientMapped: Int,

  @JvmField @SerializedName("IsPrimaryadvisor") val isPrimaryadvisor: Boolean,

  @JvmField @SerializedName("ChannelName") val channelName: Any,

  @JvmField @SerializedName("TotalClientMapped") val totalClientMapped: Int,

  @JvmField @SerializedName("UserId") val userId: Int,

  @JvmField @SerializedName("BusinessUnit") val businessUnit: Any,

  @JvmField @SerializedName("AreaCode") val areaCode: Any,

  @JvmField @SerializedName("Id") val id: Int,

  @JvmField @SerializedName("EmployeeName") val employeeName: String?,

  @JvmField @SerializedName("UserType") val userType: Int,

  @JvmField @SerializedName("BranchCode") val branchCode: String?,

  @JvmField @SerializedName("CommodityTraderCode") val commodityTraderCode: Any,

  @JvmField @SerializedName("PrimaryRM") val primaryRM: Any,

  @JvmField @SerializedName("BusinessProfileName") val businessProfileName: Any
)