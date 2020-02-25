package com.mosl.tabapp.leadViewDetail.document

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import com.mosl.mobile.domain.leadviewdetail.document.UpdateDocumentsInput
import io.reactivex.Observable
import javax.inject.Inject

class DocumentPresenter @Inject constructor(
  view: DocumentView,
  val repository: DocumentRepository
) : BasePresenter<DocumentView>(view) {
  override fun start() {
    var documentId = ""
    var documentName = ""

    Observable.merge(onFetchCalled(), onRetryCalled())
      .switchMap {
        repository.getLeadDocuments(
          view.getLeadDocumentInput()
        )
      }
      .compose(applyUiModel())
      .subscribe {
        view.renderLeadDocuments(it)
      }
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

  }

}