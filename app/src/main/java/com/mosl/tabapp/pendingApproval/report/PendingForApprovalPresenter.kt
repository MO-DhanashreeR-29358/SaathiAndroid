package com.mosl.tabapp.pendingApproval.report

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalListRequest
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.tabapp.constants.MOSLConstants.REPORT_PAGE_SIZE
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class PendingForApprovalPresenter @Inject constructor(
  view: PendingForApprovalView,
  val repository: PendingForApprovalRepository,
  val userManager: UserManagerImpl
) : BasePresenter<PendingForApprovalView>(view) {

  override fun start() {

    Observable.merge(onFetchCalled(), onRetryCalled())
      .switchMap {
        repository.getPendingApproval()
          .compose(applyUiModel())
      }
      .subscribe(view::render)
      .addTo(disposable)

    val empChangedObs: Observable<Int> = view.onEmployeeChange()
      .filter(InvalidSelectionPredicate)
      .doOnNext { position -> Timber.d("Employee changed %s", position) }
      .share()

    val profileChangedObs: Observable<Int> = view.onProfileChange()
      .filter(InvalidSelectionPredicate)
      .doOnNext { position -> Timber.d("Profile changed %s", position) }
      .share()

    val dateChangedObs: Observable<Int> = view.onDatePickerEvent()
      .doOnNext { position -> Timber.d("Date changed %s", position) }
      .share()

    val combine: Observable<Boolean> =
      Observable.combineLatest(empChangedObs, dateChangedObs, profileChangedObs,
        Function3 { _, _, _ ->
          view.getEmployee().updatedBy.isNotEmpty() &&
              view.getProfile().key.isNotEmpty() && true
        })
    combine.map { ignored -> getWidgetRequest() }
      .switchMap {
        repository.getPendingApprovalList(it)
          .compose(applyUiModel())
      }
      .subscribe(view::renderApprovalList)
      .addTo(disposable)

  }

  private fun getWidgetRequest(): PendingForApprovalListRequest {
    return PendingForApprovalListRequest(
      updatedBy = view.getEmployee().updatedBy,
      totalCount = "1",
      pageSize = REPORT_PAGE_SIZE,
      employeeCode = userManager.getUser()?.employeeCode,
      calculatedPage = "0",
      updatedStartDate = view.fromDate(),
      updatedEndDate = view.toDate(),
      entityField = view.getProfile().key
    )
  }

}