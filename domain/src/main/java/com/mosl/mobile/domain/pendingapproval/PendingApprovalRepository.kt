package com.mosl.mobile.domain.pendingapproval

import io.reactivex.rxjava3.core.Observable

interface PendingApprovalRepository {

  fun getPendingApproval(): Observable<PendingApprovalData>

}