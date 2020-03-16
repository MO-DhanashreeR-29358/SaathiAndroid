package com.mosl.tabapp

import com.mosl.mobile.pdf.OTPFragment
import com.mosl.mobile.pdf.OTPModule
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryModule
import com.mosl.tabapp.calendar.CalendarAppointsListFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.calendar.CalendarFragmentNew
import com.mosl.tabapp.calendar.CalendarModule
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
import com.mosl.tabapp.dashboard.clients.ClientsFragmentNew
import com.mosl.tabapp.dashboard.clients.ClientsModule
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsModule
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
import com.mosl.tabapp.leadViewDetail.DocumentFragment
import com.mosl.tabapp.leadViewDetail.LeadActivitiesDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadProfilingFragment
import com.mosl.tabapp.leadViewDetail.LeadViewActivitiesFragment
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
import com.mosl.tabapp.setappointment.SetAppointmentModule
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.ui.ReportFragment
import com.mosl.tabapp.ui.SecondLevelReportFragment
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
  fun provideDocumentFactory(): DocumentFragment

  @ContributesAndroidInjector
  fun provideDashboardFragmentFactory(): DashboardFragment

  @ContributesAndroidInjector
  fun provideCalendarFragmentFactory(): CalendarFragment

  @ContributesAndroidInjector(modules = [CalendarModule::class])
  fun provideCalendarFragmentNewFactory(): CalendarFragmentNew

  @ContributesAndroidInjector
  fun providePendingApprovalFragmentFactory(): PendingApprovalFragment

  @ContributesAndroidInjector(modules = [BirthdayAnniversaryModule::class])
  fun provideBirthdayAnniversaryFragmentNewFactory(): BirthdayAnniversaryFragmentNew

  @ContributesAndroidInjector
  fun provideBirthdayAnniversaryFragmentFactory(): BirthdayAnniversaryFragment

  @ContributesAndroidInjector(modules = [QueryModule::class])
  fun provideQueryFragmentFactory(): QueryFragment

  @ContributesAndroidInjector
  fun provideWebviewFragmentFactory(): WebViewFragment

  @ContributesAndroidInjector
  fun provideClientLeadsFactory(): ClientsLeadsFragment

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

  @ContributesAndroidInjector
  fun provideUpdateMeetingFragmentFactory(): UpdateMeetingFragment

  @ContributesAndroidInjector
  fun provideTargetAchievementFragmentFactory(): TargetAchievementFragment

  @ContributesAndroidInjector
  fun provideDashboardHierarchyFragmentFactory(): DashboardHierarchyFragment

  @ContributesAndroidInjector
  fun provideDashboardSecondLevelFragmentFactory(): DashboardSecondLevelFragment

  @ContributesAndroidInjector
  fun provideBusinessSnapFragmentFactory(): BusinessSnapshotFragment

  @ContributesAndroidInjector
  fun provideBusinessSnapAdvFragmentFactory(): BusSnapshotAdvFragment

  @ContributesAndroidInjector
  fun provideClientsFragmentFactory(): ClientsFragment

  @ContributesAndroidInjector
  fun provideClientLeadFragmentFactory(): ClientsLeadShowFragment

  @ContributesAndroidInjector
  fun provideClientTradedFragmentFactory(): ClientsTradedYesterdayFragment

  @ContributesAndroidInjector
  fun provideProductivityFragmentFactory(): ProductivityFragment

  @ContributesAndroidInjector
  fun provideTargetAchievementTeamFragmentFactory(): TargetAchievementTeamFragment

  @ContributesAndroidInjector
  fun provideTrendsFragmentFactory(): TrendsTabFragment

  @ContributesAndroidInjector
  fun provideNotificationFragmentFactory(): NotificationFragment

  @ContributesAndroidInjector
  fun providePendingForApprovalFragmentFactory(): PendingForApprovalFragment

  @ContributesAndroidInjector
  fun provideReportFragmentFactory(): ReportFragment

  @ContributesAndroidInjector
  fun provideSecondLevelReportFragmentFactory(): SecondLevelReportFragment

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
