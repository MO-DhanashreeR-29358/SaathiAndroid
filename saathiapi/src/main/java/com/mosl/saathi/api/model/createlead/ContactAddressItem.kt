package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class ContactAddressItem(

  @JvmField @SerializedName("CountryID") val countryID: Any,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("CountryName") val countryName: Any,

  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,

  @JvmField @SerializedName("StateName") val stateName: Any,

  @JvmField @SerializedName("Address2") val address2: String,

  @JvmField @SerializedName("StateID") val stateID: Any,

  @JvmField @SerializedName("Address3") val address3: Any,

  @JvmField @SerializedName("ContactID") val contactID: Int,

  @JvmField @SerializedName("Address1") val address1: String,

  @JvmField @SerializedName("CityName") val cityName: Any,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("AddressTypeID") val addressTypeID: Int,

  @JvmField @SerializedName("CityID") val cityID: Any,

  @JvmField @SerializedName("isPrimary") val isPrimary: Boolean,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("ContactAddressID") val contactAddressID: Int,

  @JvmField @SerializedName("AddressTypeName") val addressTypeName: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("Pincode") val pincode: Any
)