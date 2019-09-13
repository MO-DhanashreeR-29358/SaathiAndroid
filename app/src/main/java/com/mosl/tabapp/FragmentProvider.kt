package com.mosl.tabapp

import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.query.QueryFragmentNew
import com.mosl.tabapp.query.QueryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
//  @ContributesAndroidInjector(modules = [QueryModule::class])
//  abstract fun provideQueryFragmentFactory(): QueryFragmentNew

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
}