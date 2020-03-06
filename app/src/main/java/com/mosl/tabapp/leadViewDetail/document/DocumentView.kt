package com.mosl.tabapp.leadViewDetail.document

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentData
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.DeleteLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.RenameLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.UploadImage
import io.reactivex.Observable

interface DocumentView : BaseView {
  fun getLeadDocumentInput(): DocumentInput
  fun onAddDocumentClick(): Observable<Unit>
  fun renderLeadDocuments(uiModel: UiModel<List<LeadDocumentsItemData>>)
  fun renameLeadDocuments(): Observable<RenameLeadDocuments>
  fun deleteLeadDocuments(): Observable<DeleteLeadDocuments>
  fun renderUpdateDocuments(
    uiModel: UiModel<String>,
    id: String,
    documentName: String
  )

  fun uploadImage(): Observable<UploadImage>
  fun renderShowAddDocumentDialog()
  fun renderUploadDocument(uiModel: UiModel<UploadDocumentData>)
  fun renderDeleteDocument(
    uiModel: UiModel<String>,
    id: String
  )

}