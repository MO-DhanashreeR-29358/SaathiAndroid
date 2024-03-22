package com.mosl.mobile.domain.reactivation

import io.reactivex.rxjava3.core.Observable


interface ReactivationRepository {
    fun getReactivationSourceList(): Observable<List<String>>

    fun getReactivationSummaryForSales(period : String, source : String): Observable<ReactivationSummaryItem>

    fun getReactivationSummaryForTL(period : String, source : String): Observable<ReactivationSummaryItem>

    fun getReactivationDetailForSales(period : String, stage : String, source : String): Observable<ReactivationDetailCombo>

    fun getReactivationDetailForTL(employeeCode : String,firstReportingTo : String,period : String, stage : String, source : String): Observable<ReactivationDetailCombo>

    fun getReactivationAssignedHierarchyClientReportList(period : String, stage : String, source : String): Observable<ReactivationDetailCombo>

    fun getReactivationFeedback(request:ReactivationFeedbackItem):Observable<ReactivationFeedbackResponse>

    fun fnGetSearchClientCode(objReactivationClientSearchPostModel: ReactivationClientSearchPostModel):Observable<List<ReactivationClientSearchResponseModel>>

    fun fnGetMinDate(objReactivationMinDatePostModel: ReactivationMinDatePostModel):Observable<String>

    fun fnGetActualMargin(objReactivationActualMarginPostModel:ReactivationActualMarginPostModel):Observable<String>
}