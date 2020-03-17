package com.mosl.tabapp.leadViewDetail.document

import com.mosl.app.core.data.leadviewdetail.document.DocumentRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.document.DocumentRepository
import com.mosl.tabapp.permission.ImageUploadHelper
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DocumentModule {
  @Binds
  abstract fun bindDocumentView(fragment: DocumentFragment): DocumentView

  @Binds
  abstract fun bindDocumentRepository(repository: DocumentRepositoryImpl): DocumentRepository

  @Module
  companion object {
    @Provides @JvmStatic
    fun provideImageUploadHelper(
      fragment: DocumentFragment
    ): ImageUploadHelper {
      return ImageUploadHelper(fragment)
    }
  }

}