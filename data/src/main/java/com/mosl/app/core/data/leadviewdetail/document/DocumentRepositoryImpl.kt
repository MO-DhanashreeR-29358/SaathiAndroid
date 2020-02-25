package com.mosl.app.core.data.leadviewdetail.document

import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.mobile.domain.leadviewdetail.document.UpdateDocumentsInput
import com.mosl.saathi.api.SaathiApi
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

}