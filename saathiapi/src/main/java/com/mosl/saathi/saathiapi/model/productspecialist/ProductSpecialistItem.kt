package com.mosl.saathi.saathiapi.model.productspecialist

import com.google.gson.annotations.SerializedName

data class ProductSpecialistItem(

  @SerializedName("AdvisoryType")
  @JvmField val advisoryType: Any,

  @SerializedName("BusinessProfileId")
  @JvmField val businessProfileId: Int,

  @SerializedName("RegionCode")
  @JvmField val regionCode: Any,

  @SerializedName("ProductName")
  @JvmField val productName: Any,

  @SerializedName("ProductId")
  @JvmField val productId: Int,

  @SerializedName("EquityTraderCode")
  @JvmField val equityTraderCode: Any,

  @SerializedName("BusinessChannelId")
  @JvmField val businessChannelId: Int,

  @SerializedName("EmployeeCodes")
  @JvmField val employeeCodes: Any,

  @SerializedName("clientType")
  @JvmField val clientType: Any,

  @SerializedName("LookUpName")
  @JvmField val lookUpName: Any,

  @SerializedName("SubBrokerCode")
  @JvmField val subBrokerCode: Any,

  @SerializedName("BusinessUnitName")
  @JvmField val businessUnitName: Any,

  @SerializedName("ProfileGroup")
  @JvmField val profileGroup: Any,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("IsDefault")
  @JvmField val isDefault: Boolean,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("TraderCode")
  @JvmField val traderCode: Any,

  @SerializedName("NumberofClientMapped")
  @JvmField val numberofClientMapped: Int,

  @SerializedName("IsPrimaryadvisor")
  @JvmField val isPrimaryadvisor: Boolean,

  @SerializedName("ChannelName")
  @JvmField val channelName: Any,

  @SerializedName("TotalClientMapped")
  @JvmField val totalClientMapped: Int,

  @SerializedName("UserId")
  @JvmField val userId: Int,

  @SerializedName("BusinessUnit")
  @JvmField val businessUnit: Any,

  @SerializedName("AreaCode")
  @JvmField val areaCode: Any,

  @SerializedName("Id")
  @JvmField val id: Int,

  @SerializedName("EmployeeName")
  @JvmField val employeeName: String,

  @SerializedName("UserType")
  @JvmField val userType: Int,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String,

  @SerializedName("CommodityTraderCode")
  @JvmField val commodityTraderCode: Any,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: Any,

  @SerializedName("BusinessProfileName")
  @JvmField val businessProfileName: Any
)