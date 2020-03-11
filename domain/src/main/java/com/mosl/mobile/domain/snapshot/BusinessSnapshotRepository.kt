package com.mosl.mobile.domain.snapshot

import io.reactivex.Observable

interface BusinessSnapshotRepository {
  fun getSnapshot(request: BusinessSnapInput): Observable<BusinessSnapshotResponseModel>
}