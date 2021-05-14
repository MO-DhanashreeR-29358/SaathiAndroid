package com.mosl.mobile.domain.branch

import io.reactivex.rxjava3.core.Observable


interface BranchRepository {
    fun getBranch(): Observable<List<BranchItemResponse>>
}