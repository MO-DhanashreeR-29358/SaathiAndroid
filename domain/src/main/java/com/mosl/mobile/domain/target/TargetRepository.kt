package com.mosl.mobile.domain.target

import io.reactivex.Observable

interface TargetRepository {

    fun getTargetWidget(request: TargetInput): Observable<TargetItemModel>

}