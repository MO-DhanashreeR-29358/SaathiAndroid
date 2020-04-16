package com.mosl.app.core.data.pendingapproval.report

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.ProfileModel
import com.mosl.saathi.api.model.pendingforapproval.ApprovalFieldItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovalUserItem

fun List<ApprovalUserItem>.mapToListOfEmployee(): List<EmployeeModel> {
  return safeMap { it.mapToEmployee() }

}

private fun ApprovalUserItem.mapToEmployee(): EmployeeModel {
  return EmployeeModel(
    updatedBy = updatedBy ?: "",
    name = name ?: ""
  )
}

fun List<ApprovalFieldItem>.mapToListOfProfile(): List<ProfileModel> {
  return safeMap {
    it.mapToProfile()
  }
}

private fun ApprovalFieldItem.mapToProfile(): ProfileModel {
  return ProfileModel(
    referenceName = referenceName ?: "0",
    entityField = entityField ?: ""
  )
}

