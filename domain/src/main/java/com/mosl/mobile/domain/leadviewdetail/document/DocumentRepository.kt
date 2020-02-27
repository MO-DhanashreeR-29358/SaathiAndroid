package com.mosl.mobile.domain.leadviewdetail.document

import io.reactivex.Observable

interface DocumentRepository {
  fun getLeadDocuments(request: DocumentInput): Observable<List<LeadDocumentsItemData>>
  fun postUpdateDocuments(request: UpdateDocumentsInput): Observable<String>
  fun uploadDocument(request: UploadDocumentsInput): Observable<UploadDocumentData>

}