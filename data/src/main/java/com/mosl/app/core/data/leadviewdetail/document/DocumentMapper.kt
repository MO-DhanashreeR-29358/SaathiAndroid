package com.mosl.app.core.data.leadviewdetail.document

import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.FilterRequestInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.mobile.domain.leadviewdetail.document.UpdateDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentData
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentsInput

import com.mosl.saathi.api.model.documents.DocumentsRequest
import com.mosl.saathi.api.model.documents.FilterRequest
import com.mosl.saathi.api.model.documents.LeadDocumentsItem
import com.mosl.saathi.api.model.documents.LeadDocumentsRequest
import com.mosl.saathi.api.model.documents.UpdateDocumentsRequest
import com.mosl.saathi.api.model.documents.UploadDocumentItem
import com.mosl.saathi.api.model.documents.UploadDocumentsRequest

fun DocumentInput.requestTransform(): DocumentsRequest {
  return DocumentsRequest(
    request = requestLeadDocuments.leadDocumentsTransform(),
    filter = filterRequest.filterRequestTransform(),
    isAdminMode = adminMode

  )
}

fun UpdateDocumentsInput.requestUpdateTransform(): UpdateDocumentsRequest {
  return UpdateDocumentsRequest(
    id = id,
    documentName = documentName
  )
}

fun UploadDocumentsInput.requestUploadDocumentTransform(): UploadDocumentsRequest {
  return UploadDocumentsRequest(
    leadID = leadID,
    ucid = ucid,
    documentName = documentName,
    file = file
  )
}

fun LeadDocumentsInput.leadDocumentsTransform(): LeadDocumentsRequest {
  return LeadDocumentsRequest(
    leadID = leadID,
    uCID = uCID,
    pageSize = pageSize,
    page = page,
    calculatedPage = calculatedPage,
    totalCount = totalCount
  )
}

fun FilterRequestInput.filterRequestTransform(): FilterRequest {
  return FilterRequest(
    operator = operator,
    field = field,
    filters = filters,
    exportFilter = exportFilter,
    value = value,
    export = export,
    logic = logic
  )
}

fun List<LeadDocumentsItem>.responseTransform(): List<LeadDocumentsItemData> {
  return map {
    LeadDocumentsItemData(
      id = it.id,
      documentName = it.documentName,
      creationDate = it.creationDate,
      documentFullPath = it.documentFullPath,
      documentPath = it.documentPath,
      documentType = it.documentType
    )
  }
}
