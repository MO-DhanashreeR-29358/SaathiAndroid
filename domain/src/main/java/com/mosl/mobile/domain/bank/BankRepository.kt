package com.mosl.mobile.domain.bank

import io.reactivex.rxjava3.core.Observable

interface BankRepository {
    fun getBankFilter(): Observable<List<BankCodeModel>>

    fun getBranchFilter(bankId: Int): Observable<List<BranchCodeModel>>

    fun getSubBranchFilter(branchId: Int): Observable<List<SubBranchCodeModel>>


}