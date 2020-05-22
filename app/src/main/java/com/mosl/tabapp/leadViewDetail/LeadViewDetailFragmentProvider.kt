package com.mosl.tabapp.leadViewDetail

import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesFragment
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesModule
import com.mosl.tabapp.leadViewDetail.activitydetail.LeadActivitiesDetailFragment
import com.mosl.tabapp.leadViewDetail.document.DocumentFragment
import com.mosl.tabapp.leadViewDetail.document.DocumentModule
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailPagerFragment
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailPagerModule
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfilingFragment
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfilingModule
import com.mosl.tabapp.leadViewDetail.updatemeeting.UpdateMeetingFragment
import com.mosl.tabapp.leadViewDetail.updatemeeting.UpdateMeetingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LeadViewDetailFragmentProvider {

  @ContributesAndroidInjector(modules = [LeadViewActivitiesModule::class])
  abstract fun leadViewActivitiesFragmentFactory(): LeadViewActivitiesFragment

  @ContributesAndroidInjector
  abstract fun provideLeadActivitiesDetailFactory(): LeadActivitiesDetailFragment

  @ContributesAndroidInjector(modules = [LeadProfilingModule::class])
  abstract fun provideLeadProfilingFactory(): LeadProfilingFragment

  @ContributesAndroidInjector(modules = [UpdateMeetingModule::class])
  abstract fun provideUpdateMeetingFragmentFactory(): UpdateMeetingFragment

  @ContributesAndroidInjector(modules = [DocumentModule::class])
  abstract fun provideDocumentFragmentNewFactory(): DocumentFragment

  @ContributesAndroidInjector(modules = [LeadViewDetailPagerModule::class])
  abstract fun provideLeadViewDetailPagerFragmentFactory(): LeadViewDetailPagerFragment

}