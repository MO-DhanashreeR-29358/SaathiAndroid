package com.mosl.app.core.data.branch

import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.mobile.domain.branch.BranchRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.branch.BranchRequest
import io.reactivex.Observable
import javax.inject.Inject

class BranchRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManagerImpl
) : BranchRepository {

    override fun getBranch(): Observable<List<BranchItemResponse>> {
        val branchRequest = BranchRequest(userManager.user.employeeCode)
        return saathiApi.getBranchByEmployeeCode(branchRequest).map {
            it.response?.branchItemResponseTransform()
        }
    }

}