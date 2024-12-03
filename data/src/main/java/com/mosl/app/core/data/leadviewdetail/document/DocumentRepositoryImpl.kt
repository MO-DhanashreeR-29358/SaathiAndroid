package com.mosl.app.core.data.leadviewdetail.document

import android.annotation.SuppressLint
import com.mosl.mobile.domain.leadviewdetail.document.*
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.documents.DeleteDocumentRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi,
    val saathiIndirectApi: SaathiIndirectApi,
    val userManager: UserManager
) : DocumentRepository {
  @SuppressLint("SuspiciousIndentation")
  override fun getLeadDocuments(request: DocumentInput): Observable<List<LeadDocumentsItemData>> {
    val documentRequest = request.requestTransform()
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.getLeadDocuments(documentRequest)
              .map {
                  it.data?.responseTransform() ?: mutableListOf()
              }
      }else{
          return saathiApi.getLeadDocuments(documentRequest)
              .map {
                  it.responseTransform()
              }
      }

  }

  override fun postUpdateDocuments(request: UpdateDocumentsInput): Observable<String> {
    val updateDocumentRequest = request.requestUpdateTransform()
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.postUpdateDocuments(updateDocumentRequest).map {
              it.response ?:""
          }
      }else{
          return saathiApi.postUpdateDocuments(updateDocumentRequest)
      }

  }

  override fun deleteDocuments(id: String): Observable<String> {
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.postDeleteDocuments(DeleteDocumentRequest(id = id)).map {
              it.response ?:""
          }
      }else{
          return saathiApi.postDeleteDocuments(DeleteDocumentRequest(id = id))
      }

  }

  override fun uploadDocument(request: UploadDocumentsInput): Observable<UploadDocumentData> {
    val uploadDocumentRequest = request.requestUploadDocumentTransform()
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.postUploadDocuments(uploadDocumentRequest)
              .map {
                  UploadDocumentData(
                      status = it.response?.status ?:0 ,
                      documentName = it.response?.documentName ?: "",
                      documentType = it.response?.documentType ?: "",
                      documentPath = it.response?.documentPath ?: "",
                      documentFullPath = it.response?.documentFullPath ?: "",
                      creationDate = it.response?.creationDate ?: "",
                      leadID = it.response?.leadID ?: -1,
                      base64FileString = it.response?.base64FileString ?: "",
                      createdBy = it.response?.createdBy ?: "",
                      iD = it.response?.iD ?: "",
                      uCID = it.response?.uCID ?: -1
                  )
              }

      }else{
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

}