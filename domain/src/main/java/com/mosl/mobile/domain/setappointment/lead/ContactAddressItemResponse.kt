package com.mosl.mobile.domain.setappointment.lead

data class ContactAddressItemResponse(
  @JvmField val countryID: Int,
  @JvmField val status: Int,
  @JvmField val countryName: String?,
  @JvmField val businessUnitID: Int,
  @JvmField val stateName: String?,
  @JvmField val address2: String?,
  @JvmField val stateID: Int,
  @JvmField val address3: String?,
  @JvmField val contactID: Int,
  @JvmField val address1: String?,
  @JvmField val cityName: String?,
  @JvmField val inApproval: Boolean,
  @JvmField val addressTypeID: Int,
  @JvmField val cityID: Int,
  @JvmField val isPrimary: Boolean,
  @JvmField val approvalFieldList: String?,
  @JvmField val contactAddressID: Int,
  @JvmField val addressTypeName: String?,
  @JvmField val isAdminMode: Boolean,
  @JvmField val pincode: String?
)