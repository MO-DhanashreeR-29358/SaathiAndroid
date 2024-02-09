package com.mosl.saathi.api

import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.ApiEndpoints.LoginEndPoint
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.assignto.SecondaryRmFilterRequest
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.attendance.*
import com.mosl.saathi.api.model.aubankmeeting.*
import com.mosl.saathi.api.model.aubankmeetingreport.MeetingReportRequest
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryItem
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendEmailRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendGiftRequest
import com.mosl.saathi.api.model.branch.BranchItem
import com.mosl.saathi.api.model.branch.BranchRequest
import com.mosl.saathi.api.model.calender.CalenderItem
import com.mosl.saathi.api.model.calender.CalenderRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse
import com.mosl.saathi.api.model.clientviewhistory.ClientHistoryRequest
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.*
import com.mosl.saathi.api.model.createmeeting.*
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.dashboard.CommonRequest
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountItem
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountRequest
import com.mosl.saathi.api.model.dashboard.bank.*
import com.mosl.saathi.api.model.dashboard.client.ClientItemResponse
import com.mosl.saathi.api.model.dashboard.client.ClientsRequest
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradeYesterdayRequest
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradedYesterdayItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest
import com.mosl.saathi.api.model.dashboard.incentive.IncentiveItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyRequest
import com.mosl.saathi.api.model.dashboard.lead.LeadRequest
import com.mosl.saathi.api.model.dashboard.lead.LeadResponse
import com.mosl.saathi.api.model.dashboard.lead.report.LeadsReportRequest
import com.mosl.saathi.api.model.dashboard.productivity.ProductivityRequest
import com.mosl.saathi.api.model.dashboard.reportassgin.GetLeadAssignStpItemResponse
import com.mosl.saathi.api.model.dashboard.reportassgin.GetLeadAssignStpRequest
import com.mosl.saathi.api.model.dashboard.reportassgin.STPLeadTransferRequest
import com.mosl.saathi.api.model.dashboard.snapshot.BusinessSnapshotRequest
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotItem
import com.mosl.saathi.api.model.dashboard.target.TargetAchievementItem
import com.mosl.saathi.api.model.dashboard.target.TargetItemResponse
import com.mosl.saathi.api.model.dashboard.targetnew.TargetRequest
import com.mosl.saathi.api.model.dashboard.targetteam.TargetAchievementTeamItem
import com.mosl.saathi.api.model.dashboard.targetteam.TeamTargetRequest
import com.mosl.saathi.api.model.dashboard.targetteam.TeamTargetResponse
import com.mosl.saathi.api.model.dashboard.trends.TrendsRequest
import com.mosl.saathi.api.model.documents.*
import com.mosl.saathi.api.model.fcm_token.FcmTokenRequest
import com.mosl.saathi.api.model.fcm_token.FcmTokenResponse
import com.mosl.saathi.api.model.lead.*
import com.mosl.saathi.api.model.lead.LeadProductMappingItem
import com.mosl.saathi.api.model.lead.createmeeting.MobileListRequest
import com.mosl.saathi.api.model.lead.createmeeting.MobileListResponse
import com.mosl.saathi.api.model.lead.createmeeting.PostOTPForMeetingRequest
import com.mosl.saathi.api.model.lead.createmeeting.SendOTPRequest
import com.mosl.saathi.api.model.lead.request.SearchRequest
import com.mosl.saathi.api.model.lead.request.UpdateLeadMappingRequest
import com.mosl.saathi.api.model.leadinteraction.LeadInteractionRequest
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem
import com.mosl.saathi.api.model.leadprofile.LeadProfileRequest
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest
import com.mosl.saathi.api.model.leadviewactivities.*
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineRequest
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineResponse
import com.mosl.saathi.api.model.location.LocationDataResponse
import com.mosl.saathi.api.model.location.LocationRequest
import com.mosl.saathi.api.model.login.LoginRequest
import com.mosl.saathi.api.model.login.LoginResponse
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryByEmployeeItem
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryCountItem
import com.mosl.saathi.api.model.pendingapproval.PendingApprovalRequest
import com.mosl.saathi.api.model.pendingforapproval.*
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import com.mosl.saathi.api.model.prospect.ProspectRequest
import com.mosl.saathi.api.model.prospect.ProspectResponse
import com.mosl.saathi.api.model.prospect.SuggestMeRequest
import com.mosl.saathi.api.model.query.QueryItem
import com.mosl.saathi.api.model.query.QueryRequest
import com.mosl.saathi.api.model.reactivation.*
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.api.model.reports.report.LeadReportRequest
import com.mosl.saathi.api.model.search.DiyLinkResponse
import com.mosl.saathi.api.model.search.DiyRequest
import com.mosl.saathi.api.model.search.SearchResponse
import com.mosl.saathi.api.model.splash.DigitalSalesKitMenu
import com.mosl.saathi.api.model.splash.ReportTypeResponse
import com.mosl.saathi.api.model.splash.VersionRequest
import com.mosl.saathi.api.model.splash.VersionResponse
import com.mosl.saathi.api.model.webview.CommonURLRequest
import com.mosl.saathi.api.model.webview.CommonURLResponse
import com.mosl.saathi.api.model.webview.IncentiveReportRequest
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface MyZoneApi {
    @POST("api/auth/otp")
    fun generateOTP(@Body body: GenerateOtpRequest): Observable<GenerateOtpResponse>

    @POST("api/auth/validate")
    fun validateOTP(@Body body: ValidateOtpRequest): Observable<ValidateOtpResponse>

    @GET("api/app/logout")
    fun logout(): Observable<String>

    @GET("api/app/profile")
    fun getProfile(
    ): Observable<ProfileResponse>

    @GET("api/app/branch-locations")
    fun getBranchLocations(
    ): Observable<List<BranchLocationResponse>>

    @POST("api/app/mark-attendance")
    fun markAttendance(@Body body: MarkAttendanceRequest): Observable<AttendanceResponse>

    @POST("api/app/mark-Outattendance")
    fun markOutAttendance(@Body body: MarkAttendanceRequest): Observable<AttendanceResponse>

    @POST("HrmsApi/api/AddAttendancePopupLog")
    fun logAttendancePopShown(@Body body: LogAttendancePopUpShownRequest): Observable<String>

    @Headers(NO_TOKEN)
    @POST("auth/refresh")
    fun getRefreshToken(@Body tokenRequest: TokenRequest): Observable<TokenResponse>

    companion object {
        const val NO_TOKEN = "No-Token: true"
    }
}