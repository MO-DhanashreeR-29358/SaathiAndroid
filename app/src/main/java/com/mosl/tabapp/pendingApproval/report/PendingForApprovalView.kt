package com.mosl.tabapp.pendingApproval.report

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalDropdownData
import com.mosl.tabapp.common.RowData
import io.reactivex.Observable

interface PendingForApprovalView : BaseView {
  fun render(uiModel: UiModel<PendingForApprovalDropdownData>)
  fun onEmployeeChange(): Observable<Int>
  fun onProfileChange(): Observable<Int>
  fun getEmployee(): EmployeeModel
  fun getProfile(): RowData
  fun fromDate(): String
  fun toDate(): String
  fun onDatePickerEvent(): Observable<Int>
}