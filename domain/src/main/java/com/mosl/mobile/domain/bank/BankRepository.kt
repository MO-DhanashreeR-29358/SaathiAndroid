package com.mosl.mobile.domain.bank

import io.reactivex.rxjava3.core.Observable

interface BankRepository {
    fun getBankFilter(): Observable<List<BankCodeModel>>

    fun getBranchFilter(bankId: String): Observable<List<BranchCodeModel>>

    fun getSubBranchFilter(branchId: String): Observable<List<SubBranchCodeModel>>


}