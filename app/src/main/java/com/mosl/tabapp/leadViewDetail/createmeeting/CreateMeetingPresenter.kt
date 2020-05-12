package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BasePresenter
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
  }
}