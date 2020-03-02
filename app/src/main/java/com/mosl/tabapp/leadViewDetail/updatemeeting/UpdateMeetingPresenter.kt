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


    onFetchOrOnRetryObs.subscribe {
      repository.getMeetingObjective()
          .compose(applyUiModel())
    }
        .addTo(disposable)

  }
}