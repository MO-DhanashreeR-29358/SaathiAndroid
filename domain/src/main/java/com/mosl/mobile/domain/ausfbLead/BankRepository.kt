package com.mosl.mobile.domain.ausfbLead

import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.ausfbLead.SubBranchCodeModel
import io.reactivex.Observable

interface BankRepository {
    fun getBankFilter(): Observable<List<BankCodeModel>>

    fun getBranchFilter(bankId: Int): Observable<List<BranchCodeModel>>

    fun getSubBranchFilter(branchId: Int): Observable<List<SubBranchCodeModel>>


}