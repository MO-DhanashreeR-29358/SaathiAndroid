package com.mosl.mobile.domain.branch

import io.reactivex.Observable

interface BranchRepository {
    fun getBranch(): Observable<List<BranchItemResponse>>
}