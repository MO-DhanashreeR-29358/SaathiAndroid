package com.mosl.mobile.domain.saathiIndirect.dashboard

import io.reactivex.rxjava3.core.Observable
interface DashboardIndirctRepository {

    fun fnGetFOCODashboardFilter() :Observable<String>
}