package com.mosl.tabapp.branch

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.branch.BranchRepository
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class BranchPresenter @Inject constructor(
    view: BranchContract.View,
    val repository: BranchRepository
) : BasePresenter<BranchContract.View>(view) {
    override fun start() {

        onFetchCalled()
            .switchMap {
                repository.getBranch()
                    .compose(applyUiModel())
            }
            .subscribe {
                view.renderBranch(it)
            }
            .addTo(disposable)

        view.onItemClick()
            .subscribe {
                view.renderItemClick(it)
            }

    }
}