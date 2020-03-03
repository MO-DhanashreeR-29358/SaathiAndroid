package com.mosl.tabapp.leadViewDetail.updatemeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.UpdateMeetingRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class UpdateMeetingPresenter @Inject constructor(
  view: UpdateMeetingView,
  val repository: UpdateMeetingRepository,
  val data: UpdateMeetingScreenData
) : BasePresenter<UpdateMeetingView>(view) {

  override fun start() {

    val onFetchOrOnRetryObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .share()

    onFetchOrOnRetryObs
        .switchMap {
          repository.getMeeting(data.meetingId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderMeetingById)
        .addTo(disposable)


    onFetchOrOnRetryObs.switchMap {
      repository.getMeetingObjective()
          .compose(applyUiModel())
    }
        .subscribe(view::renderMeetingObjective)
        .addTo(disposable)


    view.onAttendeeImageButtonClicked()
        .subscribe { view.renderAttendeeImageButtonClick() }
        .addTo(disposable)

    view.onSplAttendeeImageButtonClicked()
        .subscribe { view.renderSplAttendeeImageButtonClick() }
        .addTo(disposable)

    view.onAttendeeCountClicked()
        .subscribe { view.renderAttendeeCountClick() }
        .addTo(disposable)

    view.onSplAttendeeCountClicked()
        .subscribe { view.renderSplAttendeeCountClick() }
        .addTo(disposable)

    view.onSaveButtonClicked()
        .subscribe { view.renderSaveButtonClick() }
        .addTo(disposable)

    view.onResetButtonClicked()
        .subscribe { view.renderResetButtonClick() }
        .addTo(disposable)

  }
}