package com.mosl.tabapp.dashboard.report.assign

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.LeadAssignItem
import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.MultiUpdateApprovalResponse
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalDropdownData
import io.reactivex.Observable

interface DashboardReportAssignlView : BaseView {
    fun render(uiModel: UiModel<PendingForApprovalDropdownData>)
    fun renderLeadAssignStpList(uiModel: UiModel<List<LeadAssignItem>>)
    fun onEmployeeChange(): Observable<Int>
    fun getEmployee(): EmployeeModel
    fun onRefreshEvent(): Observable<Int>
    fun onSelectedApprovalList(): Observable<DashboardReportAssignUIEvent.CheckBoxSelectedArrayList>
    fun onBtnEvent(): Observable<DashboardReportAssignUIEvent.BtnEvent>
    fun selectedApprovalList(arrayList: ArrayList<String>)
    fun multiUpdateApproval(uiModel: UiModel<MultiUpdateApprovalResponse>)
    fun getSelectedAppovalList(): MutableList<String>

}