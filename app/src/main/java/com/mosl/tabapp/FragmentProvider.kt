package com.mosl.tabapp

import com.mosl.mobile.pdf.OTPFragment
import com.mosl.mobile.pdf.OTPModule
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarAppointsListFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.createMeeting.CreateMeetingModule
import com.mosl.tabapp.dashboard.*
import com.mosl.tabapp.dashboard.clients.ClientsFragmentNew
import com.mosl.tabapp.dashboard.clients.ClientsModule
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsModule
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.target.TargetModule
import com.mosl.tabapp.leadViewDetail.*
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingForApprovalFragment
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.ui.ReportFragment
import com.mosl.tabapp.ui.SecondLevelReportFragment
import com.mosl.tabapp.ui.WebViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
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

    @ContributesAndroidInjector
    fun providePendingApprovalFragmentFactory(): PendingApprovalFragment

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

    @ContributesAndroidInjector
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

}