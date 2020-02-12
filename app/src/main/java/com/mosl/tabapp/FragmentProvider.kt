package com.mosl.tabapp

import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarAppointsListFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.createMeeting.CreateMeetingModule
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
import com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsFragment
import com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsModule
import com.mosl.tabapp.dashboard.ausfb.target.AuTargetFragment
import com.mosl.tabapp.dashboard.ausfb.target.AuTargetModule
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment
import com.mosl.tabapp.leadViewDetail.DocumentFragment
import com.mosl.tabapp.leadViewDetail.LeadProfilingFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragmentProvider
import com.mosl.tabapp.leadViewDetail.LeadViewHistoryFragment
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadViewDetail.UpdateMeetingFragment
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingForApprovalFragment
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.setappointment.SetAppointmentFragment
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.ui.ReportFragment
import com.mosl.tabapp.ui.SecondLevelReportFragment
import com.mosl.tabapp.ui.WebViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [LeadViewDetailFragmentProvider::class])
abstract class FragmentProvider {

  @ContributesAndroidInjector
  abstract fun provideLeadCreationFactory(): LeadCreationFragment

  @ContributesAndroidInjector
  abstract fun provideLeadViewMainFactory(): LeadViewMainFragment

  @ContributesAndroidInjector
  abstract fun provideLeadViewDetailFactory(): LeadViewDetailFragment

  @ContributesAndroidInjector
  abstract fun provideDocumentFactory(): DocumentFragment

  @ContributesAndroidInjector
  abstract fun provideDashboardFragmentFactory(): DashboardFragment

  @ContributesAndroidInjector
  abstract fun provideCalendarFragmentFactory(): CalendarFragment

  @ContributesAndroidInjector
  abstract fun providePendingApprovalFragmentFactory(): PendingApprovalFragment

  @ContributesAndroidInjector
  abstract fun provideBirthdayAnniversaryFragmentFactory(): BirthdayAnniversaryFragment

  @ContributesAndroidInjector(modules = [QueryModule::class])
  abstract fun provideQueryFragmentFactory(): QueryFragment

  @ContributesAndroidInjector
  abstract fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector
  abstract fun provideClientLeadsFactory(): ClientsLeadsFragment

  @ContributesAndroidInjector(modules = [AuLeadsModule::class])
  abstract fun provideAuLeadsFragmentFactory(): AuLeadsFragment

  @ContributesAndroidInjector(modules = [BannerModule::class])
  abstract fun provideBannerFragmentFactory(): BannerFragment

  @ContributesAndroidInjector(modules = [AuTargetModule::class])
  abstract fun provideAuTargetVsAchievementFragmentFactory(): AuTargetFragment

  @ContributesAndroidInjector
  abstract fun provideUpdateMeetingFragmentFactory(): UpdateMeetingFragment

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

  @ContributesAndroidInjector
  abstract fun provideLeadProfilingFactory(): LeadProfilingFragment

  @ContributesAndroidInjector
  abstract fun provideLeadViewHistoryFactory(): LeadViewHistoryFragment

  @ContributesAndroidInjector
  abstract fun provodeLeadViewIntreactionFactory(): LeadViewInteractionFragment

  @ContributesAndroidInjector
  abstract fun provideSetAppointmentFactory(): SetAppointmentFragment

  @ContributesAndroidInjector(modules = [CreateMeetingModule::class])
  abstract fun provideCreateMeetingFactory(): CreateMeetingFragment

  @ContributesAndroidInjector
  abstract fun provideSMTFragmentFactory(): SMTFragment

  @ContributesAndroidInjector
  abstract fun provideCalendarAppointsListFactory(): CalendarAppointsListFragment

  @ContributesAndroidInjector
  abstract fun provideBankMeetingFactory(): BankMeetingFragment

  @ContributesAndroidInjector
  abstract fun provideBankMeetingReportFactory(): BankMeetingReportFragmentNew

}