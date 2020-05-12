package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class CreateMeetingPresenter @Inject constructor(
  view: CreateMeetingView,
  val repository: CreateMeetingRepository,
  val createMeetingData: CreateMeetingData
) : BasePresenter<CreateMeetingView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getInitialPageData(createMeetingData)
              .compose(applyUiModel())
        }
        .subscribe(view::render)
        .addTo(disposable)

    view.onMeetingLocationSelected()
        .filter(InvalidSelectionPredicate)
        .map { view.getSelectedMeetingLocation() }
        .filter {
          !it.equals(
              CreateMeetingConstants.SELECT_MEETING_LOCATION
          ) && !it.lookUpName.equals("Other", ignoreCase = false)
        }
        .switchMap {
          repository.getAddress(
              view.getSelectedMeetingLocation()?.lookUpID.toString(), createMeetingData.leadId
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderAddress)
        .addTo(disposable)

    view.onActivityTypeSelected()
        .filter(InvalidSelectionPredicate)
        .map { view.getSelectedActivityType() }
        .filter { !it.equals(CreateMeetingConstants.SELECT_ACTIVITY_TYPE) }
        .switchMap {
          repository.getLookup(
              MEETINGREASON, null, view.getSelectedActivityType()?.lookUpID.toString()
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderMeetingReason)
        .addTo(disposable)
  }

  companion object {
    const val MEETINGREASON = 14
  }

}
