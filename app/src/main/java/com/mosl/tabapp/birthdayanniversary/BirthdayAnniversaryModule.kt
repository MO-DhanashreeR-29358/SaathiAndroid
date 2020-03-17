package com.mosl.tabapp.birthdayanniversary

import dagger.Binds
import dagger.Module

@Module
abstract class BirthdayAnniversaryModule {

  @Binds
  abstract fun bindBirthdayAnniversaryView(fragment: BirthdayAnniversaryFragment): BirthdayAnniversaryView

}