package com.mosl.tabapp

import com.mosl.mobile.pdf.OTPFragment
import com.mosl.mobile.pdf.OTPModule
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryModule
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.calendar.CalendarFragmentOld
import com.mosl.tabapp.calendar.CalendarModule
import com.mosl.tabapp.calendar.calendarappoinslist.CalendarAppointsListFragment
import com.mosl.tabapp.createMeeting.CreateMeetingModule
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.dashboard.DashboardHierarchyFragmentOld
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragmentOld
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightDialogFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightModule
import com.mosl.tabapp.dashboard.clients.ClientsFragment
import com.mosl.tabapp.dashboard.clients.ClientsModule
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsModule
import com.mosl.tabapp.dashboard.leads.report.LeadsReportFragment
import com.mosl.tabapp.dashboard.leads.report.LeadsReportModule
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityModule
import com.mosl.tabapp.dashboard.report.DashboardReportFragment
import com.mosl.tabapp.dashboard.report.DashboardReportModule
import com.mosl.tabapp.dashboard.report.hierarchy.DashboardHierarchyReportFragment
import com.mosl.tabapp.dashboard.report.hierarchy.DashboardHierarchyReportModule
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotModule
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvModule
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.target.TargetModule
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetModule
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayFragment
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayModule
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsModule
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragmentProvider
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryModule
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.notification.NotificationModule
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalModule
import com.mosl.tabapp.pendingApproval.report.PendingForApprovalFragment
import com.mosl.tabapp.pendingApproval.report.PendingForApprovalModule
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.report.ReportFragment
import com.mosl.tabapp.report.ReportModule
import com.mosl.tabapp.report.hierarchy.HierarchyReportFragment
import com.mosl.tabapp.report.hierarchy.HierarchyReportModule
import com.mosl.tabapp.setappointment.SetAppointmentFragment
import com.mosl.tabapp.setappointment.SetAppointmentModule
import com.mosl.tabapp.smt.SMTFragment
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
  fun provideCalendarFragmentFactory(): CalendarFragmentOld

  @ContributesAndroidInjector(modules = [CalendarModule::class])
  fun provideCalendarFragmentNewFactory(): CalendarFragment

  @ContributesAndroidInjector(modules = [PendingApprovalModule::class])
  fun providePendingApprovalFactory(): PendingApprovalFragment

  @ContributesAndroidInjector(modules = [CalendarModule::class])
  fun provideCalendarAppointsListFragmentNewFactory(): CalendarAppointsListFragment

  @ContributesAndroidInjector(modules = [BirthdayAnniversaryModule::class])
  fun provideBirthdayAnniversaryFragmentFactory(): BirthdayAnniversaryFragment

  @ContributesAndroidInjector(modules = [QueryModule::class])
  fun provideQueryFragmentFactory(): QueryFragment

  @ContributesAndroidInjector
  fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector(modules = [LeadsModule::class])
  fun provideLeadsFragmentFactory(): LeadsFragment

  @ContributesAndroidInjector(modules = [ClientsModule::class])
  fun provideClientFragmentFactory(): ClientsFragment

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
  fun provideDashboardHierarchyFragmentFactory(): DashboardHierarchyFragmentOld

  @ContributesAndroidInjector(modules = [DashboardHierarchyReportModule::class])
  fun provideDashboardHierarchyReportFragment(): DashboardHierarchyReportFragment

  @ContributesAndroidInjector(modules = [DashboardReportModule::class])
  fun provideDashboardReportFragment(): DashboardReportFragment

  @ContributesAndroidInjector
  fun provideDashboardSecondLevelFragmentFactory(): DashboardSecondLevelFragmentOld

  @ContributesAndroidInjector(modules = [LeadsReportModule::class])
  fun provideClientLeadFragmentFactory(): LeadsReportFragment

  @ContributesAndroidInjector(modules = [ProductivityModule::class])
  fun provideProductivityFragmentFactory(): ProductivityFragment

  @ContributesAndroidInjector(modules = [NotificationModule::class])
  fun provideNotificationFragmentKttFactory(): NotificationFragment

  @ContributesAndroidInjector(modules = [PendingForApprovalModule::class])
  fun providePendingForApprovalFragmentFactory(): PendingForApprovalFragment

  @ContributesAndroidInjector(modules = [ReportModule::class])
  fun provideReportFragmentFactory(): ReportFragment

  @ContributesAndroidInjector(modules = [HierarchyReportModule::class])
  fun provideHierarchyReportFragmentFactory(): HierarchyReportFragment

  @ContributesAndroidInjector
  fun provodeLeadViewIntreactionFactory(): LeadViewInteractionFragment

  @ContributesAndroidInjector(modules = [SetAppointmentModule::class])
  fun provideSetAppointmentFactory(): SetAppointmentFragment

  @ContributesAndroidInjector(modules = [CreateMeetingModule::class])
  fun provideCreateMeetingFactory(): CreateMeetingFragment

  @ContributesAndroidInjector
  fun provideSMTFragmentFactory(): SMTFragment

  @ContributesAndroidInjector
  fun provideBankMeetingFactory(): BankMeetingFragment

  @ContributesAndroidInjector
  fun provideBankMeetingReportFactory(): BankMeetingReportFragmentNew

  @ContributesAndroidInjector(modules = [OTPModule::class])
  fun provideOTPFactory(): OTPFragment

  @ContributesAndroidInjector(modules = [BusinessSnapshotAdvModule::class])
  fun provideBusinessSnapshotAdvFragmentFactory(): BusinessSnapshotAdvFragment

  @ContributesAndroidInjector(modules = [BusinessSnapshotModule::class])
  fun provideBusinessSnapshotFragmentFactory(): BusinessSnapshotFragment

  @ContributesAndroidInjector(modules = [ClientsTradedYesterdayModule::class])
  fun provideClientTradeYesterdayFragmentFactory(): ClientsTradedYesterdayFragment

  @ContributesAndroidInjector(modules = [ActionableInsightModule::class])
  fun provideActionableInsightsFragmentfactory(): ActionableInsightDialogFragment
}
