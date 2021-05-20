package com.mosl.tabapp.branch

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.branch.BranchItemResponse
import io.reactivex.rxjava3.core.Observable


interface BranchContract {
    interface View : BaseView {
        fun renderBranch(uiModel: UiModel<List<BranchItemResponse>>)
        fun onItemClick(): Observable<String>
        fun renderItemClick(branchCode: String)
    }
}