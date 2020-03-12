package com.mosl.mobile.domain.birthdayanniversary

data class BirthdayAnniversaryItemResponse(
  @JvmField val errors: Any?,

  @JvmField val total: Int,

  @JvmField val permissions: PermissionsData,

  @JvmField val data: List<BirthdayAnniversaryListData>,

  @JvmField val extraData: Any?
)

data class PermissionsData(

  @JvmField val read: Boolean,

  @JvmField val write: Boolean,

  @JvmField val businessProfileId: Int,

  @JvmField val export: Boolean,

  @JvmField val systemName: Any?
)

data class BirthdayAnniversaryListData(
  @JvmField val clientCode: String, //0
  @JvmField val clientType: String,
  @JvmField val ucid: String, //0
  @JvmField val clientName: String,
  @JvmField val dob: String,
  @JvmField val anniversaryDate: String,
  @JvmField val noOfYearBirthday: String,
  @JvmField val backOfficeMobileNumber: String,
  @JvmField val email: String,
  @JvmField val birthdayCount: String,
  @JvmField val anniversaryCount: String,
  @JvmField val clientPotential: String,
  @JvmField val birthdayFlag: Boolean,
  @JvmField val anniversaryFlag: Boolean
)

data class ProfileSpinner(
  @JvmField val id: Int,
  @JvmField val lookUpName: String
) {
  override fun toString(): String {
    return lookUpName
  }
}

data class RequestInput(
  @JvmField val dayType: String,

  @JvmField val totalCount: String = "0",

  @JvmField val pageSize: String,

  @JvmField val occasion: String,

  @JvmField val calculatedPage: String = "0",

  @JvmField val page: String = "1"
)

data class BirthdayRequestInput(
  @JvmField var filter: FilterInput,
  @JvmField val request: RequestInput,
  @JvmField val isAdminMode: Boolean = true
)

data class FilterInput(

  @JvmField val filters: List<FiltersItemInput>,

  @JvmField val logic: String = "and"
)

data class FiltersItemInput(

  @JvmField val operator: String = "startswith",

  @JvmField val field: String = "ClientPotential",

  @JvmField var value: ArrayList<String> = arrayListOf("")
)

data class SendEmailRequestInput(
  @JvmField val selectedUcids: String?,

  @JvmField val checkAll: Boolean,

  @JvmField val eventType: String,

  @JvmField val messageType: String,

  @JvmField val employeeCode: String?,

  @JvmField val filterType: String?

)


