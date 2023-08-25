package com.mosl.app.core.data

import com.mosl.app.core.data.autarget.TargetRepositoryImpl
import com.mosl.app.core.data.bank.BankRepositoryImpl
import com.mosl.app.core.data.birthdayanniversary.BirthdayAnniversaryRepositoryImpl
import com.mosl.app.core.data.branch.BranchRepositoryImpl
import com.mosl.app.core.data.calendar.CalendarRepositoryImpl
import com.mosl.app.core.data.clients.ClientsRepositoryImpl
import com.mosl.app.core.data.dashboardreport.DashboardReportRepositoryImpl
import com.mosl.app.core.data.dashboardreport.LeadAssignRepositoryImpl
import com.mosl.app.core.data.lead.LeadRepositoryImpl
import com.mosl.app.core.data.lead.reports.LeadsReportRepositoryImpl
import com.mosl.app.core.data.leadviewdetail.history.MappingHistoryRepositoryImpl
import com.mosl.app.core.data.leadviewdetail.leaddetail.LeadDetailRepositoryImpl
import com.mosl.app.core.data.leadviewdetail.main.LeadViewDetailPagerRepositoryImpl
import com.mosl.app.core.data.notification.NotificationRepositoryImpl
import com.mosl.app.core.data.pendingapproval.PendingApprovalRepositoryImpl
import com.mosl.app.core.data.pendingapproval.report.PendingForApprovalRepositoryImpl
import com.mosl.app.core.data.productivity.ProductivityRepositoryImpl
import com.mosl.app.core.data.reactivation.ReactivationRepositoryImpl
import com.mosl.app.core.data.report.HierarchyReportRepositoryImpl
import com.mosl.app.core.data.snapshot.BusinessSnapshotRepositoryImpl
import com.mosl.app.core.data.teamtarget.TeamTargetRepositoryImpl
import com.mosl.app.core.data.tradeyesterday.ClientTradeYesterdayRepositoryImpl
import com.mosl.app.core.data.trends.TrendsRepositoryImpl
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import com.mosl.mobile.domain.branch.BranchRepository
import com.mosl.mobile.domain.calendar.CalendarRepository
import com.mosl.mobile.domain.clients.ClientsRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.LeadAssignRepository
import com.mosl.mobile.domain.lead.LeadRepository
import com.mosl.mobile.domain.lead.report.LeadsReportRepository
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryRepository
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailRepository
import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.pendingapproval.PendingApprovalRepository
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalRepository
import com.mosl.mobile.domain.productivity.ProductivityRepository
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.domain.report.HierarchyReportRepository
import com.mosl.mobile.domain.snapshot.BusinessSnapshotRepository
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.mobile.domain.teamtarget.TeamTargetRepository
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayRepository
import com.mosl.mobile.domain.trends.TrendsRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryAliasProviders {

    @Binds
    fun provideBankRepository(repository: BankRepositoryImpl): BankRepository

    @Binds
    fun provideAusfbLeadRepository(repository: LeadRepositoryImpl): LeadRepository

    @Binds
    fun provideLeadReportsRepository(repository: LeadsReportRepositoryImpl): LeadsReportRepository

    @Binds
    fun provideClientsRepositoryRepository(repository: ClientsRepositoryImpl): ClientsRepository

    @Binds
    fun provideBusinessSnapshotRepository(repository: BusinessSnapshotRepositoryImpl): BusinessSnapshotRepository

    @Binds
    fun provideProvideRepository(repository: ProductivityRepositoryImpl): ProductivityRepository

    @Binds
    fun provideMappingHistoryRepository(repository: MappingHistoryRepositoryImpl): MappingHistoryRepository

    @Binds
    fun provideTargetVsAchRepository(repository: TargetRepositoryImpl): TargetRepository

    @Binds
    fun provideTeamTargetVsAchRepository(repository: TeamTargetRepositoryImpl): TeamTargetRepository

    @Binds
    fun provideClientTradeYesterdayRepository(repository: ClientTradeYesterdayRepositoryImpl): ClientTradeYesterdayRepository

    @Binds
    fun provideTrendsRepositoryRepository(repository: TrendsRepositoryImpl): TrendsRepository

    @Binds
    fun bindsCalendarRepository(repository: CalendarRepositoryImpl): CalendarRepository

    @Binds
    fun bindsBranchRepository(repository: BranchRepositoryImpl): BranchRepository


    @Binds
    fun bindNotificationRepository(repository: NotificationRepositoryImpl): NotificationRepository

    @Binds
    fun bindpendingApprovalRepository(repository: PendingApprovalRepositoryImpl): PendingApprovalRepository

    @Binds
    fun bindPendingForApprovalRepository(repository: PendingForApprovalRepositoryImpl): PendingForApprovalRepository

    @Binds
    fun provideHierarchyReportRepository(repository: HierarchyReportRepositoryImpl): HierarchyReportRepository

    @Binds
    fun provideDashboardHierarchyReportRepository(repository: DashboardReportRepositoryImpl): DashboardReportRepository

    @Binds
    fun provideLeadAssignRepository(repository: LeadAssignRepositoryImpl): LeadAssignRepository

    @Binds
    fun bindBirthdayAnniversaryRepository(repository: BirthdayAnniversaryRepositoryImpl): BirthdayAnniversaryRepository

    @Binds
    fun provideLeadViewDetailPagerRepository(repository: LeadViewDetailPagerRepositoryImpl): LeadViewDetailPagerRepository

    @Binds
    fun bindLeadDetailRepository(repository: LeadDetailRepositoryImpl): LeadDetailRepository

    @Binds
    fun bindsReactivationRepository(repository: ReactivationRepositoryImpl): ReactivationRepository
}