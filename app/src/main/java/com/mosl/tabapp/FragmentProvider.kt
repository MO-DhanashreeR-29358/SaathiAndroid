package com.mosl.tabapp

import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.dashboard.BusSnapshotAdvFragment
import com.mosl.tabapp.dashboard.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.ClientsFragment
import com.mosl.tabapp.dashboard.ClientsLeadShowFragment
import com.mosl.tabapp.dashboard.ClientsLeadsFragment
import com.mosl.tabapp.dashboard.ClientsTradedYesterdayFragment
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.dashboard.DashboardHierarchyFragment
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragment
import com.mosl.tabapp.dashboard.ProductivityFragment
import com.mosl.tabapp.dashboard.TargetAchievementFragment
import com.mosl.tabapp.dashboard.TargetAchievementTeamFragment
import com.mosl.tabapp.dashboard.TrendsTabFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingForApprovalFragment
import com.mosl.tabapp.query.QueryFragmentNew
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.ui.ReportFragment
import com.mosl.tabapp.ui.SecondLevelReportFragment
import com.mosl.tabapp.ui.WebViewFragment
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

  @ContributesAndroidInjector
  abstract fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector
  abstract fun provideClientLeadsFactory(): ClientsLeadsFragment

  @ContributesAndroidInjector
  abstract fun provideTargetAchievementFragmentFactory(): TargetAchievementFragment

  @ContributesAndroidInjector
  abstract fun provideDashboardHierarchyFragmentFactory(): DashboardHierarchyFragment

  @ContributesAndroidInjector
  abstract fun provideDashboardSecondLevelFragmentFactory(): DashboardSecondLevelFragment

  @ContributesAndroidInjector
  abstract fun provideBusinessSnapFragmentFactory(): BusinessSnapshotFragment

  @ContributesAndroidInjector
  abstract fun provideBusinessSnapAdvFragmentFactory(): BusSnapshotAdvFragment

  @ContributesAndroidInjector
  abstract fun provideClientsFragmentFactory(): ClientsFragment

  @ContributesAndroidInjector
  abstract fun provideClientLeadFragmentFactory(): ClientsLeadShowFragment

  @ContributesAndroidInjector
  abstract fun provideClientTradedFragmentFactory(): ClientsTradedYesterdayFragment

  @ContributesAndroidInjector
  abstract fun provideProductivityFragmentFactory(): ProductivityFragment

  @ContributesAndroidInjector
  abstract fun provideTargetAchievementTeamFragmentFactory(): TargetAchievementTeamFragment

  @ContributesAndroidInjector
  abstract fun provideTrendsFragmentFactory(): TrendsTabFragment

  @ContributesAndroidInjector
  abstract fun provideNotificationFragmentFactory(): NotificationFragment

  @ContributesAndroidInjector
  abstract fun providePendingForApprovalFragmentFactory(): PendingForApprovalFragment

  @ContributesAndroidInjector
  abstract fun provideReportFragmentFactory(): ReportFragment

  @ContributesAndroidInjector
  abstract fun provideSecondLevelReportFragmentFactory(): SecondLevelReportFragment
}