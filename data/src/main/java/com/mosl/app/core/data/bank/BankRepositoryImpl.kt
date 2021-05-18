package com.mosl.app.core.data.bank

import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SubBranchCodeModel
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.bank.BranchCodeRequest
import com.mosl.saathi.api.model.dashboard.bank.SubBranchCodeRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : BankRepository {
    override fun getBankFilter(): Observable<List<BankCodeModel>> {
        return api.getBankFilter().map {
            it.banckCodeTransform()
        }
    }

    override fun getBranchFilter(bankId: String): Observable<List<BranchCodeModel>> {
        val branchCodeRequest =
            BranchCodeRequest(bankId = bankId)
        return api.getBranchFilter(branchCodeRequest).map {
            it.branchCodeTransform()
        }
    }

    override fun getSubBranchFilter(branchId: String): Observable<List<SubBranchCodeModel>> {
        val subBranchCodeRequest =
            SubBranchCodeRequest(
                bankBranchId = branchId
            )
        return api.getSubBranchFilter(subBranchCodeRequest).map {
            it.subBranchCodeTransform()
        }
    }
}