package com.mosl.app.core.data.leadviewdetail.document

import com.mosl.mobile.domain.leadviewdetail.document.*
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.documents.DeleteDocumentRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : DocumentRepository {
  override fun getLeadDocuments(request: DocumentInput): Observable<List<LeadDocumentsItemData>> {
    val documentRequest = request.requestTransform()
    return saathiApi.getLeadDocuments(documentRequest)
        .map {
          it.responseTransform()
        }
  }

  override fun postUpdateDocuments(request: UpdateDocumentsInput): Observable<String> {
    val updateDocumentRequest = request.requestUpdateTransform()
    return saathiApi.postUpdateDocuments(updateDocumentRequest)
  }

  override fun deleteDocuments(id: String): Observable<String> {
    return saathiApi.postDeleteDocuments(DeleteDocumentRequest(id = id))
  }

  override fun uploadDocument(request: UploadDocumentsInput): Observable<UploadDocumentData> {
    val uploadDocumentRequest = request.requestUploadDocumentTransform()
    return saathiApi.postUploadDocuments(uploadDocumentRequest)
        .map {
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