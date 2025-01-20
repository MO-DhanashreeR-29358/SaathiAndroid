package com.mosl.tabapp

import com.mosl.mobile.pdf.OTPModule
import com.mosl.tabapp.attendancedialog.AttendanceDialogFragment
import com.mosl.tabapp.attendancedialog.AttendanceDialogModule
import com.mosl.tabapp.attendancedialog.verifyotp.VerifyDialogFragment
import com.mosl.tabapp.attendancedialog.verifyotp.VerifyOtpModule
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerModule
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryModule
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.calendar.CalendarModule
import com.mosl.tabapp.calendar.calendarappoinslist.CalendarAppointsListFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightDialogFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightModule
import com.mosl.tabapp.dashboard.base.DashboardFragment
import com.mosl.tabapp.dashboard.base.DashboardModule
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
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignFragment
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignModule
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
import com.mosl.tabapp.drawermenu.digitalSalesKit.DigitalSalesKitFragment
import com.mosl.tabapp.drawermenu.digitalSalesKit.DigitalSalesKitModule
import com.mosl.tabapp.geoLocation.GeoTaggingFragment
import com.mosl.tabapp.geoLocation.GeoTaggingModule
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragmentProvider
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryModule
import com.mosl.tabapp.leadcreation.ui.LeadCreationFragment
import com.mosl.tabapp.leadcreation.ui.LeadCreationModule
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.notification.NotificationModule
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalModule
import com.mosl.tabapp.pendingApproval.report.PendingForApprovalFragment
import com.mosl.tabapp.pendingApproval.report.PendingForApprovalModule
import com.mosl.tabapp.qrcode.QRCodeFragment
import com.mosl.tabapp.qrcode.QRCodeModule
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.query.QueryModule
import com.mosl.tabapp.reactivation.createrequestReactivation.CreateRequestReactivationFragment
import com.mosl.tabapp.reactivation.createrequestReactivation.CreateRequestReactivationModule
import com.mosl.tabapp.reactivation.createrequestReactivationDetails.ReactivationDetailsFragment
import com.mosl.tabapp.reactivation.createrequestReactivationDetails.ReactivationDetailsModule
import com.mosl.tabapp.reactivation.detail.ReactivationDetailFragment
import com.mosl.tabapp.reactivation.detail.ReactivationDetailModule
import com.mosl.tabapp.reactivation.detailTL.ReactivationDetailTLFragment
import com.mosl.tabapp.reactivation.detailTL.ReactivationDetailTLModule
import com.mosl.tabapp.reactivation.summary.ReactivationSummaryFragment
import com.mosl.tabapp.reactivation.summary.ReactivationSummaryModule
import com.mosl.tabapp.report.ReportFragment
import com.mosl.tabapp.report.ReportModule
import com.mosl.tabapp.report.hierarchy.HierarchyReportFragment
import com.mosl.tabapp.report.hierarchy.HierarchyReportModule
import com.mosl.tabapp.saathiIndirect.LeadCreation.IndirectLeadCreationFragment
import com.mosl.tabapp.saathiIndirect.LeadCreation.IndirectLeadCreationModule
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingFragment
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report.IndirectIFADashboardReportFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report.IndirectIFADashboardReportModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead.IndirectIFAMyLeadFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead.IndirectIFAMyLeadModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot.IndirectIFABuisnessSnapshotFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot.IndirectIFABuisnessSnapshotModule
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp.IndirectIFABuisnessSnapshotIpFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp.IndirectIFABuisnessSnapshotIpModule
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardModule
import com.mosl.tabapp.saathiIndirect.dashboard.foco.FocoDashboardFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.FocoDashboardModule
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportModule
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy.FocoHierarchyReportFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy.FocoHierarchyReportModule
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetModule
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.IndirectLeadDetailMainFragment
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.IndirectLeadDetailMainModule
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentFragment
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentModule
import com.mosl.tabapp.setappointment.SetAppointmentFragment
import com.mosl.tabapp.setappointment.SetAppointmentModule
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.webview.WebviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.mosl.tabapp.saathiIndirect.prospestMeeting.IndirectProspestMeetingModule
import com.mosl.tabapp.saathiIndirect.prospestMeeting.IndirectProspestmeetingFragment

@Module(includes = [LeadViewDetailFragmentProvider::class])
interface FragmentProvider {

    @ContributesAndroidInjector(modules = [LeadCreationModule::class])
    fun provideLeadCreationFactory(): LeadCreationFragment

    @ContributesAndroidInjector
    fun provideLeadViewMainFactory(): LeadViewMainFragment

    @ContributesAndroidInjector(modules = [IndirectLeadDetailMainModule::class])
    fun provideIndirectLeadViewMainFactory(): IndirectLeadDetailMainFragment

    @ContributesAndroidInjector
    fun provideLeadViewDetailFactory(): LeadViewDetailFragment

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

    @ContributesAndroidInjector()
    fun provideWebviewFragmentFactory(): WebviewFragment

    @ContributesAndroidInjector(modules = [LeadsModule::class])
    fun provideLeadsFragmentFactory(): LeadsFragment

    @ContributesAndroidInjector(modules = [ClientsModule::class])
    fun provideClientFragmentFactory(): ClientsFragment

    @ContributesAndroidInjector(modules = [BannerModule::class])
    fun provideBannerFragmentFactory(): BannerFragment

    @ContributesAndroidInjector(modules = [TargetModule::class])
    fun provideAuTargetVsAchievementFragmentFactory(): TargetFragment

    @ContributesAndroidInjector(modules = [FocoTargetModule::class])
    fun provideFocoTargetVsAchievementFragmentFactory(): FocoTargetFragment

    @ContributesAndroidInjector(modules = [MonthOnMonthTrendsModule::class])
    fun provideMonthOnMonthTrendsFragmentFactory(): MonthOnMonthTrendsFragment

    @ContributesAndroidInjector(modules = [TeamTargetModule::class])
    fun provideTeamTargetFragmentFactory(): TeamTargetFragment

    @ContributesAndroidInjector(modules = [MappingHistoryModule::class])
    fun provideMappingHistoryFragment(): MappingHistoryFragment

    @ContributesAndroidInjector(modules = [DashboardHierarchyReportModule::class])
    fun provideDashboardHierarchyReportFragment(): DashboardHierarchyReportFragment

    @ContributesAndroidInjector(modules = [DashboardReportModule::class])
    fun provideDashboardReportFragment(): DashboardReportFragment

    @ContributesAndroidInjector(modules = [DashboardReportAssignModule::class])
    fun provideDashboardReportAssignFragment(): DashboardReportAssignFragment


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

    @ContributesAndroidInjector(modules = [DigitalSalesKitModule::class])
    fun provideDigitalSalesKitFragmentFactory(): DigitalSalesKitFragment

    @ContributesAndroidInjector(modules = [HierarchyReportModule::class])
    fun provideHierarchyReportFragmentFactory(): HierarchyReportFragment

    @ContributesAndroidInjector
    fun provodeLeadViewIntreactionFactory(): LeadViewInteractionFragment

    @ContributesAndroidInjector(modules = [SetAppointmentModule::class])
    fun provideSetAppointmentFactory(): SetAppointmentFragment

    @ContributesAndroidInjector(modules = [com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingModule::class])
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

    @ContributesAndroidInjector(modules = [DashboardModule::class])
    fun provideNewDashboardFragmentFactory(): DashboardFragment

    @ContributesAndroidInjector(modules = [GeoTaggingModule::class])
    fun provideGeoTaggingFactory(): GeoTaggingFragment

    @ContributesAndroidInjector(modules = [QRCodeModule::class])
    fun provideQRCodeFragmentFactory(): QRCodeFragment

    @ContributesAndroidInjector(modules = [ReactivationSummaryModule::class])
    fun provideReactivationSummaryFragmentFactory(): ReactivationSummaryFragment

    @ContributesAndroidInjector(modules = [ReactivationDetailModule::class])
    fun provideReactivationDetailFragmentFactory(): ReactivationDetailFragment

    @ContributesAndroidInjector(modules = [ReactivationDetailTLModule::class])
    fun provideReactivationDetailTLFragmentFactory(): ReactivationDetailTLFragment

    @ContributesAndroidInjector(modules = [CreateRequestReactivationModule::class])
    fun provideCreateRequestReactivationFragmentFactory(): CreateRequestReactivationFragment

    @ContributesAndroidInjector(modules = [ReactivationDetailsModule::class])
    fun provideReactivationDetailsFragmentFactory(): ReactivationDetailsFragment

    @ContributesAndroidInjector(modules = [AttendanceDialogModule::class])
    fun provideAttendanceFragmentFactory(): AttendanceDialogFragment

    @ContributesAndroidInjector(modules = [VerifyOtpModule::class])
    fun provideVerifyDialogFragmentFactory(): VerifyDialogFragment

    @ContributesAndroidInjector(modules = [IndirectDashboardModule::class])
    fun provideIndirectDashboardFragmentFactory(): IndirectDashboardFragment

    @ContributesAndroidInjector(modules = [FocoDashboardModule::class])
    fun provideIndirectFocoDashboardFragmentFactory(): FocoDashboardFragment

    @ContributesAndroidInjector(modules = [FocoDashboardReportModule::class])
    fun provideIndirectFocoDashboardReportFragmentFactory(): FocoDashboardReportFragment

    @ContributesAndroidInjector(modules = [FocoHierarchyReportModule::class])
    fun provideIndirectFocoHierarchyReportFragmentFactory(): FocoHierarchyReportFragment

    @ContributesAndroidInjector(modules = [IndirectLeadCreationModule::class])
    fun provideIndirectLeadCreationFactory(): IndirectLeadCreationFragment

    @ContributesAndroidInjector(modules=[IndirectSetAppointmentModule::class])
    fun provideIndirectSetAppointmentFragmentFactory(): IndirectSetAppointmentFragment

    @ContributesAndroidInjector (modules = [IndirectCreateMeetingModule::class])
    fun provideIndirectCreateMeetingFragmentFactory():IndirectCreateMeetingFragment

    @ContributesAndroidInjector(modules=[IndirectProspestMeetingModule::class])
    fun provideIndirectProspectMeetingFragmentFactory():IndirectProspestmeetingFragment

    @ContributesAndroidInjector(modules = [IndirectIFADashboardModule::class])
    fun provideIndirectIFADashboardFragmentFactory():IndirectIFADashboardFragment

    @ContributesAndroidInjector(modules = [IndirectIFADashboardDetailsModule::class])
    fun provideIndirectIFADashboardDetailsFragmentFactory():IndirectIFADashboardDetailsFragment

    @ContributesAndroidInjector(modules = [IndirectIFATargetModule::class])
    fun provideIndirectIFATargetFragmentFactory():IndirectIFATargetFragment

    @ContributesAndroidInjector(modules = [IndirectIFAUniqueModule::class])
    fun provideIndirectIFAUniqueFragmentFactory():IndirectIFAUniqueFragment

    @ContributesAndroidInjector(modules=[IndirectIFADashboardReportModule::class])
    fun provideIndirectIFADashboardReportFragmentFactory():IndirectIFADashboardReportFragment

    @ContributesAndroidInjector(modules = [IndirectIFABuisnessSnapshotModule::class])
    fun provideIndirectIFABuisnessSnapshotFragmentFactory():IndirectIFABuisnessSnapshotFragment

    @ContributesAndroidInjector(modules = [IndirectIFAMyLeadModule::class])
    fun provideIndirectIFAMyLeadFragmentFactory():IndirectIFAMyLeadFragment

    @ContributesAndroidInjector(modules = [IndirectIFABuisnessSnapshotIpModule::class])
    fun provideIndirectIFABuisnessSnapshotIpFragmentFactory():IndirectIFABuisnessSnapshotIpFragment

}
