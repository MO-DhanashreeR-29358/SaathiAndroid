package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class NewUser(

	@field:SerializedName("BusinessProfiles")
	val businessProfiles: List<Any?>? = null,

	@field:SerializedName("DefaultBusinessProfile")
	val defaultBusinessProfile: DefaultBusinessProfile? = null,

	@field:SerializedName("Products")
	val products: Any? = null,

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("UserTypeSeq")
	val userTypeSeq: Int? = null,

	@field:SerializedName("LastLoginDate")
	val lastLoginDate: String? = null,

	@field:SerializedName("Token")
	val token: String? = null,

	@field:SerializedName("IsSystemUser")
	val isSystemUser: Boolean? = null,

	@field:SerializedName("Profile")
	val profile: String? = null,

	@field:SerializedName("ResignedDate")
	val resignedDate: Any? = null,

	@field:SerializedName("FirstReportingManager")
	val firstReportingManager: String? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("BIDashboards")
	val bIDashboards: List<Any?>? = null,

	@field:SerializedName("IsEKYCRevamp")
	val isEKYCRevamp: Int? = null,

	@field:SerializedName("EmployeeCode")
	val employeeCode: String? = null,

	@field:SerializedName("CompanyId")
	val companyId: Any? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("SecondReportingManager")
	val secondReportingManager: String? = null,

	@field:SerializedName("ImageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("AvailabilityStatus")
	val availabilityStatus: Int? = null,

	@field:SerializedName("AppId")
	val appId: Int? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("LastName")
	val lastName: Any? = null,

	@field:SerializedName("Base64ImageUrl")
	val base64ImageUrl: Any? = null,

	@field:SerializedName("BIReport")
	val bIReport: List<Any?>? = null,

	@field:SerializedName("CurrentBusinessProfile")
	val currentBusinessProfile: NewCurrentBusinessProfile? = null
)

data class NewCurrentBusinessProfile(

	@field:SerializedName("BusinessProfileId")
	val businessProfileId: Int? = null,

	@field:SerializedName("ProductName")
	val productName: Any? = null,

	@field:SerializedName("RegionCode")
	val regionCode: Any? = null,

	@field:SerializedName("ProductId")
	val productId: Int? = null,

	@field:SerializedName("Name")
	val name: Any? = null,

	@field:SerializedName("EquityTraderCode")
	val equityTraderCode: Any? = null,

	@field:SerializedName("BusinessChannelId")
	val businessChannelId: Int? = null,

	@field:SerializedName("SubBrokerCode")
	val subBrokerCode: Any? = null,

	@field:SerializedName("BusinessUnitName")
	val businessUnitName: Any? = null,

	@field:SerializedName("ProfileGroup")
	val profileGroup: Any? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("IsDefault")
	val isDefault: Boolean? = null,

	@field:SerializedName("EmployeeCode")
	val employeeCode: Any? = null,

	@field:SerializedName("BusinessUnitId")
	val businessUnitId: Int? = null,

	@field:SerializedName("UserRoles")
	val userRoles: List<Any?>? = null,

	@field:SerializedName("TraderCode")
	val traderCode: Any? = null,

	@field:SerializedName("Active")
	val active: Boolean? = null,

	@field:SerializedName("ChannelName")
	val channelName: Any? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("AreaCode")
	val areaCode: Any? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("UserType")
	val userType: Int? = null,

	@field:SerializedName("BranchCode")
	val branchCode: Any? = null,

	@field:SerializedName("CommodityTraderCode")
	val commodityTraderCode: Any? = null,

	@field:SerializedName("BusinessProfileName")
	val businessProfileName: Any? = null
)

data class DefaultBusinessProfile(

	@field:SerializedName("BusinessProfileId")
	val businessProfileId: Int? = null,

	@field:SerializedName("ProductName")
	val productName: Any? = null,

	@field:SerializedName("RegionCode")
	val regionCode: Any? = null,

	@field:SerializedName("ProductId")
	val productId: Int? = null,

	@field:SerializedName("Name")
	val name: Any? = null,

	@field:SerializedName("EquityTraderCode")
	val equityTraderCode: Any? = null,

	@field:SerializedName("BusinessChannelId")
	val businessChannelId: Int? = null,

	@field:SerializedName("SubBrokerCode")
	val subBrokerCode: Any? = null,

	@field:SerializedName("BusinessUnitName")
	val businessUnitName: Any? = null,

	@field:SerializedName("ProfileGroup")
	val profileGroup: Any? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("IsDefault")
	val isDefault: Boolean? = null,

	@field:SerializedName("EmployeeCode")
	val employeeCode: Any? = null,

	@field:SerializedName("BusinessUnitId")
	val businessUnitId: Int? = null,

	@field:SerializedName("UserRoles")
	val userRoles: List<Any?>? = null,

	@field:SerializedName("TraderCode")
	val traderCode: Any? = null,

	@field:SerializedName("Active")
	val active: Boolean? = null,

	@field:SerializedName("ChannelName")
	val channelName: Any? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("AreaCode")
	val areaCode: Any? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("UserType")
	val userType: Int? = null,

	@field:SerializedName("BranchCode")
	val branchCode: Any? = null,

	@field:SerializedName("CommodityTraderCode")
	val commodityTraderCode: Any? = null,

	@field:SerializedName("BusinessProfileName")
	val businessProfileName: Any? = null
)
