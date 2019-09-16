package com.mosl.tabapp

import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.dashboard.ClientsLeadsFragment
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragment
import com.mosl.tabapp.dashboard.TargetAchievementFragment
import com.mosl.tabapp.leadViewDetail.DocumentFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.query.QueryFragmentNew
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.ui.WebViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {

  @ContributesAndroidInjector
  abstract fun provideLeadCreationFragment(): LeadCreationFragment

  @ContributesAndroidInjector
  abstract fun provideLeadViewMainFragment(): LeadViewMainFragment
  @ContributesAndroidInjector
  abstract fun provideLeadViewDetailFragment(): LeadViewDetailFragment

  @ContributesAndroidInjector
  abstract fun provideDocumentFragment(): DocumentFragment

  @ContributesAndroidInjector
  abstract fun provideDashboardFragmentFactory(): DashboardFragment

  @ContributesAndroidInjector
  abstract fun provideCalendarFragmentFactory(): CalendarFragment

  @ContributesAndroidInjector
  abstract fun providePendingApprovalFragmentFactory(): PendingApprovalFragment

  @ContributesAndroidInjector
  abstract fun provideBirthdayAnniversaryFragmentFactory(): BirthdayAnniversaryFragment

  @ContributesAndroidInjector(modules = [QueryModule::class])
  abstract fun provideQueryFragmentFactory(): QueryFragmentNew

  @ContributesAndroidInjector
  abstract fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector
  abstract fun provideClientLeadsFactory(): ClientsLeadsFragment

  @ContributesAndroidInjector
  abstract fun provideTargetAchievementFragmentFactory(): TargetAchievementFragment

  @ContributesAndroidInjector
  abstract fun provideDashboardSecondLevelFragmentFactory(): DashboardSecondLevelFragment
}