package com.mosl.tabapp.leadcreation

import com.mosl.tabapp.common.RowData

data class LeadCreationUserInput(
  @JvmField val selectedCategory: RowData,
  @JvmField val etCategoryName: String,
  @JvmField val etContactPerson: String,
  @JvmField val selectedInterestIn: RowData,
  @JvmField val selectedProductType: RowData,
  @JvmField val emailId: String,
  @JvmField val alternateEmailId: String,
  @JvmField val mobileNo: String,
  @JvmField val alternateMobileNo: String,
  @JvmField val landlineNo: String,
  @JvmField val ekycMobileNo: String,
  @JvmField val panNo: String,
  @JvmField val aadharNo: String,
  @JvmField val sourceName: String,
  @JvmField val refereeCode: String,
  @JvmField val refereeEmail: String,
  @JvmField val refereeName: String,
  @JvmField val refereeMobile: String,
  @JvmField val pinCode: String,
  @JvmField val officePinCode: String
)