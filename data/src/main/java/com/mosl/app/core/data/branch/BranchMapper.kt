package com.mosl.app.core.data.branch

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.saathi.api.model.branch.BranchItem


fun List<BranchItem>.branchItemResponseTransform(): List<BranchItemResponse> {
    return safeMap {
        BranchItemResponse(
            branchCode = it.branchCode
        )
    }
}

