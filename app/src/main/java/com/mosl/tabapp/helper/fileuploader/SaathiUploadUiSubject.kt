package com.mosl.tabapp.helper.fileuploader

sealed  class SaathiUploadUiSubject {
    data class SelectedImageEvent(
        val uriString: String,
        val docType: String,
        val uploadProofType: String = "0"
    ) : SaathiUploadUiSubject()
    data class SelectedFileEvent(val uriString: String) : SaathiUploadUiSubject()
}