package com.mosl.tabapp.pendingApproval.report

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalRepository
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import javax.inject.Inject

class PendingForApprovalPresenter @Inject constructor(
  view: PendingForApprovalView,
  val repository: PendingForApprovalRepository,
  private val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<PendingForApprovalView>(view) {

  override fun start() {

    Observable.merge(onFetchCalled(), onRetryCalled())
      .switchMap {
        repository.getPendingApproval()
          .compose(applyUiModel())
      }
      .subscribe(view::render)
      .addTo(disposable)

  }

}