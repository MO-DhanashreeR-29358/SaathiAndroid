package com.mosl.app.core.data.reactivation

import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.mobile.domain.branch.BranchRepository
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.branch.BranchRequest
import com.mosl.saathi.api.model.reactivation.ReactivationSummaryRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ReactivationRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManagerImpl
) : ReactivationRepository {

    override fun getReactivationSummary(period : String): Observable<ReactivationSummaryItem> {
        val request = ReactivationSummaryRequest(userManager.user.employeeCode, period)
        return saathiApi.getReactivationSummary(request).map {
            it.response?.data.first().summaryItemResponseTransform()
        }
    }

}