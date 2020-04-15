package com.mosl.mobile.domain.pendingapproval

import io.reactivex.Observable

interface PendingApprovalRepository {

  fun getPendingApproval(): Observable<PendingApprovalData>

}