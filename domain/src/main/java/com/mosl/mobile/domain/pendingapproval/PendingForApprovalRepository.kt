package com.mosl.mobile.domain.pendingapproval

import io.reactivex.rxjava3.core.Observable

interface PendingForApprovalRepository {

  fun getPendingApproval(): Observable<PendingForApprovalDropdownData>
  fun getPendingApprovalList(pendingForApprovalListRequest: PendingForApprovalListRequest): Observable<List<PendingForApprovalItem>>
  fun getMultiUpdateApproval(multiUpdateApprovalModel: MultiUpdateApprovalModel): Observable<MultiUpdateApprovalResponse>

}