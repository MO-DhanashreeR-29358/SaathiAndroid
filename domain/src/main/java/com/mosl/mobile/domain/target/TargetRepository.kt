package com.mosl.mobile.domain.target

import io.reactivex.Observable

interface TargetRepository {

    fun getAuTargetWidget(request: TargetRequest): Observable<TargetItemModel>

}