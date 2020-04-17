package com.mosl.app.core.data.pendingapproval.report

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.MultiUpdateApprovalModel
import com.mosl.mobile.domain.pendingapproval.MultiUpdateApprovalResponse
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalItem
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalListRequest
import com.mosl.mobile.domain.pendingapproval.ProfileModel
import com.mosl.saathi.api.model.pendingforapproval.ApprovalFieldItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovalListItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovalListRequest
import com.mosl.saathi.api.model.pendingforapproval.ApprovalUserItem
import com.mosl.saathi.api.model.pendingforapproval.MultiUpdateApprovalRequest

fun List<ApprovalUserItem>.mapToListOfEmployee(): List<EmployeeModel> {
  return safeMap { it.mapToEmployee() }

}

fun List<ApprovalListItem>.mapToApprovalList(): List<PendingForApprovalItem> {
  return safeMap { it.mapToPendingForApproval() }

}

private fun ApprovalListItem.mapToPendingForApproval(): PendingForApprovalItem {
  return PendingForApprovalItem(
    approvalRequestId = approvalRequestId,
    updatedBy = updatedBy,
    leadID = leadID,
    referenceName = referenceName,
    employeeName = employeeName,
    approvalFieldValue = approvalFieldValue,
    ucid = uCID,
    clientName = clientName,
    clientCode = clientCode,
    entity = entity,
    finalStatus = finalStatus,
    updationDate = updationDate,
    approver2StatusUpdateDate = approver2StatusUpdateDate ?: ""
  )
}

private fun ApprovalUserItem.mapToEmployee(): EmployeeModel {
  return EmployeeModel(
    updatedBy = updatedBy ?: "",
    name = name ?: ""
  )
}

fun mapToMultiUpdateApprovalresponse(
  response: String?,
  message: String
): MultiUpdateApprovalResponse {
  return MultiUpdateApprovalResponse(
    response = response,
    message = message

  )
}

fun PendingForApprovalListRequest.mapToListRequest(): ApprovalListRequest {

  return ApprovalListRequest(
    updatedBy = updatedBy,
    totalCount = totalCount,
    pageSize = pageSize,
    employeeCode = employeeCode ?: "",
    calculatedPage = calculatedPage,
    updatedStartDate = updatedStartDate,
    updatedEndDate = updatedEndDate,
    entityField = entityField

  )

}

fun MultiUpdateApprovalModel.mapToMultiApprovalrRquest(): MultiUpdateApprovalRequest {

  return MultiUpdateApprovalRequest(
    status,
    approvalRequestId

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

