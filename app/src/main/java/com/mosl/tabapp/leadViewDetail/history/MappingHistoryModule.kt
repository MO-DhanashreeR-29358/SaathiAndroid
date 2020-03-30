package com.mosl.tabapp.leadViewDetail.history

import dagger.Module
import dagger.Provides

@Module
class MappingHistoryModule {
  @Provides
  internal fun provideView(fragment: MappingHistoryFragment): MappingHistoryView {
    return fragment
  }

}