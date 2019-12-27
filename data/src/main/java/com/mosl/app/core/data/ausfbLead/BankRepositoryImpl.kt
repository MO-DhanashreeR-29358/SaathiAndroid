package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.BankRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.ausfbLead.*
import io.reactivex.Observable
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : BankRepository {
    override fun getBankFilter(): Observable<List<BankCodeModel>> {
        return api.getBankFilter().map {
            it.banckCodeTransform()
        }
    }

    override fun getBranchFilter(bankId: Int): Observable<List<BranchCodeModel>> {
        val branchCodeRequest = BranchCodeRequest(bankId = bankId.toString())
        return api.getBranchFilter(branchCodeRequest).map {
            it.branchCodeTransform()
        }
    }

    override fun getSubBranchFilter(branchId: Int): Observable<List<SubBranchCodeModel>> {
        val subBranchCodeRequest = SubBranchCodeRequest(bankBranchId = branchId.toString())
        return api.getSubBranchFilter(subBranchCodeRequest).map {
            it.subBranchCodeTransform()
        }
    }
}