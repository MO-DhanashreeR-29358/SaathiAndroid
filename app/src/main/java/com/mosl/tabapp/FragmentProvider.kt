package com.mosl.tabapp

import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
//  @ContributesAndroidInjector(modules = [QueryModule::class])
//  abstract fun provideQueryFragmentFactory(): QueryFragmentNew

  @ContributesAndroidInjector
  abstract fun provideLeadCreationFragment(): LeadCreationFragment

  @ContributesAndroidInjector
  abstract fun provideLeadViewDetailFragment(): LeadViewDetailFragment
}