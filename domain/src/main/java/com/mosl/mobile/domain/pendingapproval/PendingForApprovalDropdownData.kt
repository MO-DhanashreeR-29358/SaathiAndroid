package com.mosl.mobile.domain.pendingapproval

data class PendingForApprovalDropdownData(
  @JvmField val listOfEmployee: List<EmployeeModel>,
  @JvmField val listOfProfile: List<ProfileModel>
)

data class ProfileModel(
  @JvmField val entityField: String,
  @JvmField val referenceName: String
) {
  override fun toString(): String {
    return referenceName
  }
}

data class EmployeeModel(
  @JvmField val updatedBy: String,
  @JvmField val name: String
) {
  override fun toString(): String {
    return name
  }
}

