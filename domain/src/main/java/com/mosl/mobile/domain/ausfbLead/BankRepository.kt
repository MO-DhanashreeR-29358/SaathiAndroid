package com.mosl.mobile.domain.ausfbLead

import com.mosl.saathi.api.model.dashboard.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.SubBranchCodeModel
import io.reactivex.Observable

interface BankRepository {
    fun getBankFilter(): Observable<List<BankCodeModel>>

    fun getBranchFilter(bankId: Int): Observable<List<BranchCodeModel>>

    fun getSubBranchFilter(branchId: Int): Observable<List<SubBranchCodeModel>>


}