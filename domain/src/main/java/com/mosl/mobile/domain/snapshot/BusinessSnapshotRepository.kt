package com.mosl.mobile.domain.snapshot

import io.reactivex.rxjava3.core.Observable

interface BusinessSnapshotRepository {
  fun getSnapshot(request: BusinessSnapInput): Observable<BusinessSnapshotResponseModel>

  fun getDashboardData(request: BusinessSnapInput): Observable<BusinessSnapshotCombo>
}