package com.mosl.mobile.domain.reactivation

import io.reactivex.rxjava3.core.Observable


interface ReactivationRepository {
    fun getReactivationSummary(period : String): Observable<ReactivationSummaryItem>
}