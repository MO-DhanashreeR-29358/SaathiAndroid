package com.mosl.mobile.domain.leadviewdetail.history

import io.reactivex.rxjava3.core.Observable

interface MappingHistoryRepository {
  fun getDashboardData(
    leadId: String,
    ucId: String,
    eventType: String
  ): Observable<MappingHistoryCombo>

  fun getIndirectLeadHistoryData(
    leadId: String,
    ucId: String
  ): Observable<MappingHistoryCombo>

  fun getIndirectLeadMappingHistoryData(
    leadId: String,
    ucId: String
  ): Observable<MappingHistoryCombo>
}
