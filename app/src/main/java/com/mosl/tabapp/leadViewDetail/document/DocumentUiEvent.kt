package com.mosl.tabapp.leadViewDetail.document

sealed class DocumentUiEvent {
  data class RenameLeadDocuments(
    @JvmField val id: String,
    @JvmField val documentName: String
  ) : DocumentUiEvent()

  data class DeleteLeadDocuments(@JvmField val id: String) : DocumentUiEvent()
  data class UploadImage(
    @JvmField val leadId: String,
    @JvmField val ucid: String,
    @JvmField val fileName: String,
    @JvmField val strData: String
  ) :
      DocumentUiEvent()
}