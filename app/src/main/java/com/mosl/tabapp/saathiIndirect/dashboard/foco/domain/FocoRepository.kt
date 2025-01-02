package com.mosl.tabapp.saathiIndirect.dashboard.foco.domain

import io.reactivex.rxjava3.core.Observable

interface FocoRepository {

    fun getPeriodFilter(request: FocoPeriodFilterInput): Observable<List<FocoPeriodModel>>

}