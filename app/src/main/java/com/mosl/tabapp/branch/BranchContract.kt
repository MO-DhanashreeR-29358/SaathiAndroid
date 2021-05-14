package com.mosl.tabapp.branch

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.branch.BranchItemResponse


interface BranchContract {
    interface View : BaseView {
        fun renderBranch(uiModel: UiModel<List<BranchItemResponse>>)
    }
}