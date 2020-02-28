package com.mosl.app.core.data.leadviewdetail.document

import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.mobile.domain.leadviewdetail.document.UpdateDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentData
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentsInput
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.documents.DeleteDocumentRequest
import io.reactivex.Observable
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : DocumentRepository {
  override fun getLeadDocuments(request: DocumentInput): Observable<List<LeadDocumentsItemData>> {
    val documentRequest = request.requestTransform()
    return api.getLeadDocuments(documentRequest).map {
      it.responseTransform()
    }
  }

  override fun postUpdateDocuments(request: UpdateDocumentsInput): Observable<String> {
    val updateDocumentRequest = request.requestUpdateTransform()
    return api.postUpdateDocuments(updateDocumentRequest)
  }

  override fun deleteDocuments(id: String): Observable<String> {
    return api.postDeleteDocuments(DeleteDocumentRequest(id = id))
  }

  override fun uploadDocument(request: UploadDocumentsInput): Observable<UploadDocumentData> {
    val uploadDocumentRequest = request.requestUploadDocumentTransform()
    return api.postUploadDocuments(uploadDocumentRequest).map {
      UploadDocumentData(
        status = it.status,
        documentName = it.documentName ?: "",
        documentType = it.documentType ?: "",
        documentPath = it.documentPath ?: "",
        documentFullPath = it.documentFullPath ?: "",
        creationDate = it.creationDate ?: "",
        leadID = it.leadID ?: -1,
        base64FileString = it.base64FileString ?: "",
        createdBy = it.createdBy ?: "",
        iD = it.iD ?: "",
        uCID = it.uCID ?: -1
      )
    }
  }

}