package com.mosl.mobile.domain.target

import io.reactivex.rxjava3.core.Observable

interface TargetRepository {

    fun getTargetWidget(request: TargetInput): Observable<TargetItemModel>

}