package com.mosl.mobile.domain.leadviewdetail.history

import io.reactivex.Observable

interface MappingHistoryRepository {
  fun getDashboardData(
    leadId: String,
    ucId: String,
    eventType: String
  ): Observable<MappingHistoryCombo>
}