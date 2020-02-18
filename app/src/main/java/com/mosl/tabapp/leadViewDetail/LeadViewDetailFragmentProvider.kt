package com.mosl.tabapp.leadViewDetail

import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesFragment
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesModule
import com.mosl.tabapp.leadViewDetail.activitydetail.LeadActivitiesDetailFragment
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfilingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LeadViewDetailFragmentProvider {

  @ContributesAndroidInjector(modules = [LeadViewActivitiesModule::class])
  abstract fun LeadViewActivitiesFragmentFactory(): LeadViewActivitiesFragment

  @ContributesAndroidInjector
  abstract fun provideLeadActivitiesDetailFactory(): LeadActivitiesDetailFragment

  @ContributesAndroidInjector
  abstract fun provideLeadProfilingFactory(): LeadProfilingFragment

}