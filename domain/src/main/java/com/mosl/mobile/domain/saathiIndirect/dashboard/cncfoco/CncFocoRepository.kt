package com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco

import io.reactivex.rxjava3.core.Observable

interface CncFocoRepository {

    fun fnGetCncDashboardFilter():Observable<String>
}