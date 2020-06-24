package com.mosl.tabapp.dashboard.report.assign

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.LeadAssignItem
import com.mosl.mobile.domain.dashboardreport.STPLeadTransferResponse
import com.mosl.tabapp.common.FilterRowData
import io.reactivex.Observable

interface DashboardReportAssignlView : BaseView {
    fun renderLeadAssignStpList(uiModel: UiModel<List<LeadAssignItem>>)
    fun onEmployeeFilterChange(): Observable<Int>
    fun getEmployeeFilter(): FilterRowData?
    fun onRefreshEvent(): Observable<Int>
    fun onSelectedApprovalList(): Observable<DashboardReportAssignUIEvent.CheckBoxSelectedArrayList>
    fun onBtnEvent(): Observable<DashboardReportAssignUIEvent.BtnEvent>
    fun selectedApprovalList(arrayList: ArrayList<String>)
    fun multiUpdateApproval(uiModel: UiModel<STPLeadTransferResponse>)
    fun getSelectedAppovalList(): MutableList<String>
    fun renderEmployeeFilter(typeFilter: List<FilterRowData>)

}