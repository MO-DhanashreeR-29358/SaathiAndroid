package com.mosl.mobile.domain.autarget

import io.reactivex.Observable

interface AuTargetRepository {

    fun getAuTargetWidget(request: AuTargetInput): Observable<TargetItemModel>

}