package com.mosl.mobile.user.model

import com.google.gson.annotations.SerializedName

data class NewUser(
    @JvmField val businessProfiles: List<Any?>? = null,

    @JvmField val defaultBusinessProfile: DefaultBusinessProfile? = null,

    @JvmField val products: Any? = null,

    @JvmField val email: String? = null,

    @JvmField val userTypeSeq: Int? = null,

    @JvmField val lastLoginDate: String? = null,

    @JvmField val token: String? = null,

    @JvmField val isSystemUser: Boolean? = null,

    @JvmField val profile: String? = null,

    @JvmField val resignedDate: Any? = null,

    @JvmField val firstReportingManager: String? = null,

    @JvmField val status: Int? = null,

    @JvmField val bIDashboards: List<Any?>? = null,

    @JvmField val isEKYCRevamp: Int? = null,

    @JvmField val employeeCode: String? = null,

    @JvmField val companyId: Any? = null,

    @JvmField val firstName: String? = null,

    @JvmField val secondReportingManager: String? = null,

    @JvmField val imageUrl: String? = null,

    @JvmField val mobile: String? = null,

    @JvmField val availabilityStatus: Int? = null,

    @JvmField val appId: Int? = null,

    @JvmField val id: Int? = null,

    @JvmField val lastName: Any? = null,

    @JvmField val base64ImageUrl: Any? = null,

    @JvmField val bIReport: List<Any?>? = null,

    @JvmField val currentBusinessProfile: NewCurrentBusinessProfile? = null
)
data class NewCurrentBusinessProfile(

    @JvmField val businessProfileId: Int? = null,

    @JvmField val productName: Any? = null,

    @JvmField val regionCode: Any? = null,

    @JvmField val productId: Int? = null,

    @JvmField val name: Any? = null,

    @JvmField val equityTraderCode: Any? = null,

    @JvmField val businessChannelId: Int? = null,

    @JvmField val subBrokerCode: Any? = null,

    @JvmField val businessUnitName: Any? = null,

    @JvmField val profileGroup: Any? = null,

    @JvmField val status: Int? = null,

    @JvmField val isDefault: Boolean? = null,

    @JvmField val employeeCode: Any? = null,

    @JvmField val businessUnitId: Int? = null,

    @JvmField val userRoles: List<Any?>? = null,

    @JvmField val traderCode: Any? = null,

    @JvmField val active: Boolean? = null,

    @JvmField val channelName: Any? = null,

    @JvmField val userId: Int? = null,

    @JvmField val areaCode: Any? = null,

    @JvmField val id: Int? = null,

    @JvmField val userType: Int? = null,

    @JvmField val branchCode: Any? = null,

    @JvmField val commodityTraderCode: Any? = null,

    @JvmField val businessProfileName: Any? = null
)

data class DefaultBusinessProfile(
    @JvmField val businessProfileId: Int? = null,

    @JvmField val productName: Any? = null,

    @JvmField val regionCode: Any? = null,

    @JvmField val productId: Int? = null,

    @JvmField val name: Any? = null,

    @JvmField val equityTraderCode: Any? = null,

    @JvmField val businessChannelId: Int? = null,

    @JvmField val subBrokerCode: Any? = null,

    @JvmField val businessUnitName: Any? = null,

    @JvmField val profileGroup: Any? = null,

    @JvmField val status: Int? = null,

    @JvmField val isDefault: Boolean? = null,

    @JvmField val employeeCode: Any? = null,

    @JvmField val businessUnitId: Int? = null,

    @JvmField val userRoles: List<Any?>? = null,

    @JvmField val traderCode: Any? = null,

    @JvmField val active: Boolean? = null,

    @JvmField val channelName: Any? = null,

    @JvmField val userId: Int? = null,

    @JvmField val areaCode: Any? = null,

    @JvmField val id: Int? = null,

    @JvmField val userType: Int? = null,

    @JvmField val branchCode: Any? = null,

    @JvmField val commodityTraderCode: Any? = null,

    @JvmField val businessProfileName: Any? = null
)