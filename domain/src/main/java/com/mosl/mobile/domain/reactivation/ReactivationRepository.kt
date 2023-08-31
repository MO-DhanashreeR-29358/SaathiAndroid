package com.mosl.mobile.domain.reactivation

import io.reactivex.rxjava3.core.Observable


interface ReactivationRepository {
    fun getReactivationSummaryForSales(period : String): Observable<ReactivationSummaryItem>

    fun getReactivationSummaryForTL(period : String): Observable<ReactivationSummaryItem>

    fun getReactivationDetailForSales(period : String, stage : String): Observable<ReactivationDetailCombo>

    fun getReactivationAssignedHierarchyClientReportList(period : String, stage : String): Observable<ReactivationDetailCombo>

    fun getReactivationFeedback(request:ReactivationFeedbackItem):Observable<ReactivationFeedbackResponse>
}