package com.mosl.mobile.domain.pendingapproval

import io.reactivex.Observable

interface PendingForApprovalRepository {

  fun getPendingApproval(): Observable<PendingForApprovalDropdownData>
  fun getPendingApprovalList(request: PendingForApprovalListRequest): Observable<String>

}