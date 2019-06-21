package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class ContactAddressItem(
  @JvmField @SerializedName("CountryID") val countryID: Int,
  @JvmField @SerializedName("Status") val status: Int,
  @JvmField @SerializedName("CountryName") val countryName: String?,
  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,
  @JvmField @SerializedName("StateName") val stateName: String?,
  @JvmField @SerializedName("Address2") val address2: String?,
  @JvmField @SerializedName("StateID") val stateID: Int,
  @JvmField @SerializedName("Address3") val address3: String?,
  @JvmField @SerializedName("ContactID") val contactID: Int,
  @JvmField @SerializedName("Address1") val address1: String?,
  @JvmField @SerializedName("CityName") val cityName: String?,
  @JvmField @SerializedName("InApproval") val inApproval: Boolean,
  @JvmField @SerializedName("AddressTypeID") val addressTypeID: Int,
  @JvmField @SerializedName("CityID") val cityID: Int,
  @JvmField @SerializedName("isPrimary") val isPrimary: Boolean,
  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: String?,
  @JvmField @SerializedName("ContactAddressID") val contactAddressID: Int,
  @JvmField @SerializedName("AddressTypeName") val addressTypeName: String?,
  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,
  @JvmField @SerializedName("Pincode") val pincode: String?
)