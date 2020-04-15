package com.mosl.tabapp.pendingApproval

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.pendingapproval.PendingApprovalRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import javax.inject.Inject

class PendingApprovalPresenter @Inject constructor(
  view: PendingApprovalView,
  val repository: PendingApprovalRepository,
  private val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<PendingApprovalView>(view) {

  override fun start() {

    Observable.merge(onFetchCalled(), onRetryCalled())
      .switchMap {
        repository.getPendingApproval()
          .compose(applyUiModel())
      }
      .subscribe(view::render)
      .addTo(disposable)
    view.onItemClicked()
      .subscribe {
        navigateTo(it.entityField)
      }
      .addTo(disposable)

  }

  private fun navigateTo(entityField: String) {
    val args = Bundle()
    args.putString(MOSLConstants.ENTITY_FIELD, entityField)
    screenNavigator.openPage(
      NavigationConstants.NAVIGATE_TO_PENDING_FOR_APPROVAL,
      args,
      true,
      false
    )
  }

}