package com.mosl.tabapp.leadViewDetail.document

import com.mosl.app.core.data.leadviewdetail.document.DocumentRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DocumentModule {
  @Binds
  abstract fun bindDocumentView(fragment: DocumentFragmentNew): DocumentView

  @Binds
  abstract fun bindDocumentRepository(repository: DocumentRepositoryImpl): DocumentRepository

}