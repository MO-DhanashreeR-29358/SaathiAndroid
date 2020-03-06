package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.data.birthdayanniversary.BirthdayAnniversaryRepositoryImpl
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BirthdayAnniversaryModule {

  @Binds
  abstract fun bindBirthdayAnniversaryView(fragment: BirthdayAnniversaryFragmentNew): BirthdayAnniversaryView

  @Binds
  abstract fun bindBirthdayAnniversaryRepository(repository: BirthdayAnniversaryRepositoryImpl): BirthdayAnniversaryRepository
}