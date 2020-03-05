package com.mosl.tabapp.leadViewDetail.document

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import com.mosl.mobile.domain.leadviewdetail.document.UpdateDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentsInput
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DocumentPresenter @Inject constructor(
  view: DocumentView,
  val repository: DocumentRepository
) : BasePresenter<DocumentView>(view) {
  override fun start() {
    var documentId = ""
    var documentName = ""

    Observable.merge(onFetchCalled(), onRetryCalled())
        .map { view.getLeadDocumentInput() }
        .switchMap {
          repository.getLeadDocuments(it)
              .compose(applyRvUiModel())
        }
        .subscribe({
          view.renderLeadDocuments(it)
        }, {
          Timber.d(it)
        })
        .addTo(disposable)

    view.renameLeadDocuments()
        .map {
          documentId = it.id
          documentName = it.documentName
          UpdateDocumentsInput(
              it.id,
              it.documentName
          )
        }
        .switchMap {
          repository.postUpdateDocuments(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderUpdateDocuments(it, documentId, documentName)
        }
        .addTo(disposable)

    view.deleteLeadDocuments()
        .map {
          it.id
        }
        .switchMap {
          documentId = it
          repository.deleteDocuments(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderDeleteDocument(it, documentId)
        }
        .addTo(disposable)


    view.onAddDocumentClick()
        .subscribe {
          view.renderShowAddDocumentDialog()
        }
        .addTo(disposable)

    view.uploadImage()
        .map {
          UploadDocumentsInput(
              it.leadId, it.ucid, it.fileName, it.strData
          )

        }
        .switchMap {
          repository.uploadDocument(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderUploadDocument(it)
        }
        .addTo(disposable)

  }

}