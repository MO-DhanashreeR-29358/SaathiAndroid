package com.mosl.tabapp

import com.mosl.mobile.pdf.OTPFragment
import com.mosl.mobile.pdf.OTPModule
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryModule
import com.mosl.tabapp.calendar.CalendarAppointsListFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.createMeeting.CreateMeetingModule
import com.mosl.tabapp.dashboard.BusSnapshotAdvFragmentOld
import com.mosl.tabapp.dashboard.BusinessSnapshotFragmentOld
import com.mosl.tabapp.dashboard.ClientsFragmentOld
import com.mosl.tabapp.dashboard.ClientsLeadsFragmentOld
import com.mosl.tabapp.dashboard.ClientsTradedYesterdayFragmentOld
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.dashboard.DashboardHierarchyFragment
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragment
import com.mosl.tabapp.dashboard.ProductivityFragmentOld
import com.mosl.tabapp.dashboard.TargetAchievementFragmentOld
import com.mosl.tabapp.dashboard.TargetAchievementTeamFragmentOld
import com.mosl.tabapp.dashboard.TrendsTabFragmentOld
import com.mosl.tabapp.dashboard.clients.ClientsFragmentNew
import com.mosl.tabapp.dashboard.clients.ClientsModule
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsModule
import com.mosl.tabapp.dashboard.leads.report.LeadsReportFragment
import com.mosl.tabapp.dashboard.leads.report.LeadsReportModule
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityModule
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotModule
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvModule
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.target.TargetModule
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetModule
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayModule
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsModule
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment
import com.mosl.tabapp.leadViewDetail.LeadActivitiesDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadProfilingFragment
import com.mosl.tabapp.leadViewDetail.LeadViewActivitiesFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragmentProvider
import com.mosl.tabapp.leadViewDetail.LeadViewHistoryFragment
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadViewDetail.UpdateMeetingFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryModule
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.notification.NotificationModule
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingForApprovalFragment
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.report.HierarchyReportFragment
import com.mosl.tabapp.report.HierarchyReportModule
import com.mosl.tabapp.setappointment.SetAppointmentFragment
import com.mosl.tabapp.setappointment.SetAppointmentModule
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.ui.ReportFragment
import com.mosl.tabapp.ui.WebViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [LeadViewDetailFragmentProvider::class])
interface FragmentProvider {

  @ContributesAndroidInjector
  fun provideLeadCreationFactory(): LeadCreationFragment

  @ContributesAndroidInjector
  fun provideLeadViewMainFactory(): LeadViewMainFragment

  @ContributesAndroidInjector
  fun provideLeadViewDetailFactory(): LeadViewDetailFragment

  @ContributesAndroidInjector
  fun provideDashboardFragmentFactory(): DashboardFragment

  @ContributesAndroidInjector
  fun provideCalendarFragmentFactory(): CalendarFragment

  @ContributesAndroidInjector
  fun providePendingApprovalFragmentFactory(): PendingApprovalFragment

  @ContributesAndroidInjector(modules = [BirthdayAnniversaryModule::class])
  fun provideBirthdayAnniversaryFragmentFactory(): BirthdayAnniversaryFragment

  @ContributesAndroidInjector(modules = [QueryModule::class])
  fun provideQueryFragmentFactory(): QueryFragment

  @ContributesAndroidInjector
  fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector
  fun provideClientLeadsFactory(): ClientsLeadsFragmentOld

  @ContributesAndroidInjector(modules = [LeadsModule::class])
  fun provideLeadsFragmentFactory(): LeadsFragment

  @ContributesAndroidInjector(modules = [ClientsModule::class])
  fun provideClientFragmentFactory(): ClientsFragmentNew

  @ContributesAndroidInjector(modules = [BannerModule::class])
  fun provideBannerFragmentFactory(): BannerFragment

  @ContributesAndroidInjector(modules = [TargetModule::class])
  fun provideAuTargetVsAchievementFragmentFactory(): TargetFragment

  @ContributesAndroidInjector(modules = [MonthOnMonthTrendsModule::class])
  fun provideMonthOnMonthTrendsFragmentFactory(): MonthOnMonthTrendsFragment

  @ContributesAndroidInjector(modules = [TeamTargetModule::class])
  fun provideTeamTargetFragmentFactory(): TeamTargetFragment

  @ContributesAndroidInjector(modules = [MappingHistoryModule::class])
  fun provideMappingHistoryFragment(): MappingHistoryFragment

  @ContributesAndroidInjector
  fun provideUpdateMeetingFragmentFactory(): UpdateMeetingFragment

  @ContributesAndroidInjector
  fun provideTargetAchievementFragmentFactory(): TargetAchievementFragmentOld

  @ContributesAndroidInjector
  fun provideDashboardHierarchyFragmentFactory(): DashboardHierarchyFragment

  @ContributesAndroidInjector
  fun provideDashboardSecondLevelFragmentFactory(): DashboardSecondLevelFragment

  @ContributesAndroidInjector
  fun provideBusinessSnapFragmentFactory(): BusinessSnapshotFragmentOld

  @ContributesAndroidInjector
  fun provideBusinessSnapAdvFragmentFactory(): BusSnapshotAdvFragmentOld

  @ContributesAndroidInjector
  fun provideClientsFragmentFactory(): ClientsFragmentOld

  @ContributesAndroidInjector(modules = [LeadsReportModule::class])
  fun provideClientLeadFragmentFactory(): LeadsReportFragment

  @ContributesAndroidInjector
  fun provideClientTradedFragmentFactory(): ClientsTradedYesterdayFragmentOld

  @ContributesAndroidInjector
  fun provideProductivityFragmentOldFactory(): ProductivityFragmentOld

  @ContributesAndroidInjector(modules = [ProductivityModule::class])
  fun provideProductivityFragmentFactory(): ProductivityFragment

  @ContributesAndroidInjector
  fun provideTargetAchievementTeamFragmentFactory(): TargetAchievementTeamFragmentOld

  @ContributesAndroidInjector
  fun provideTrendsFragmentFactory(): TrendsTabFragmentOld

  @ContributesAndroidInjector(modules = [NotificationModule::class])
  fun provideNotificationFragmentKttFactory(): NotificationFragment

  @ContributesAndroidInjector
  fun providePendingForApprovalFragmentFactory(): PendingForApprovalFragment

  @ContributesAndroidInjector
  fun provideReportFragmentFactory(): ReportFragment

  @ContributesAndroidInjector(modules = [HierarchyReportModule::class])
  fun provideHierarchyReportFragmentFactory(): HierarchyReportFragment

  @ContributesAndroidInjector
  fun provideLeadViewActivitieFactory(): LeadViewActivitiesFragment

  @ContributesAndroidInjector
  fun provideLeadActivitiesDetailFactory(): LeadActivitiesDetailFragment

  @ContributesAndroidInjector
  fun provideLeadProfilingFactory(): LeadProfilingFragment

  @ContributesAndroidInjector
  fun provideLeadViewHistoryFactory(): LeadViewHistoryFragment

  @ContributesAndroidInjector
  fun provodeLeadViewIntreactionFactory(): LeadViewInteractionFragment

  @ContributesAndroidInjector(modules = [SetAppointmentModule::class])
  fun provideSetAppointmentFactory(): SetAppointmentFragment

  @ContributesAndroidInjector(modules = [CreateMeetingModule::class])
  fun provideCreateMeetingFactory(): CreateMeetingFragment

  @ContributesAndroidInjector
  fun provideSMTFragmentFactory(): SMTFragment

  @ContributesAndroidInjector
  fun provideCalendarAppointsListFactory(): CalendarAppointsListFragment

  @ContributesAndroidInjector
  fun provideBankMeetingFactory(): BankMeetingFragment

  @ContributesAndroidInjector
  fun provideBankMeetingReportFactory(): BankMeetingReportFragmentNew

  @ContributesAndroidInjector(modules = [OTPModule::class])
  fun provideOTPFactory(): OTPFragment

  @ContributesAndroidInjector(modules = [BusinessSnapshotAdvModule::class])
  fun provideBusinessSnapshotAdvFragmentFactory(): BusinessSnapshotAdvFragment

  @ContributesAndroidInjector(modules = [BusinessSnapshotModule::class])
  fun provideBusinessSnapshotFragmentFactory(): com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment

  @ContributesAndroidInjector(modules = [ClientsTradedYesterdayModule::class])
  fun provideClientTradeYesterdayFragmentFactory(): com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayFragment
}
