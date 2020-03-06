package com.mosl.tabapp.setappointment

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import javax.inject.Inject

class SetAppointmentPresenter @Inject constructor(
  view: SetAppointmentView,
  val repository: SetAppointmentRepository
) : BasePresenter<SetAppointmentView>(view) {
  override fun start() {

    view.getClientDetailData()
        .map {
          it.Id
        }
        .switchMap {
          repository.getClientDetail(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderClientDetail(it)
        }
        .addTo(disposable)

    view.getProfileImage()
        .map {
          it.Id
        }
        .switchMap {
          repository.getClientImage(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderProfileImage(it)
        }
        .addTo(disposable)

    view.getLeadViews()
        .map {
          it.Id
        }
        .switchMap {
          repository.getLeadViewById(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderLeadView(it)
        }
        .addTo(disposable)

    view.getMeetingById()
        .map {
          it.meetingId
        }
        .switchMap {
          repository.getMeetingById(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderMeetingByID(it)
        }
        .addTo(disposable)


    view.getProduct()
        .map {
          it.request
        }
        .switchMap {
          repository.getAllLookUp(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderProduct(it)
        }
        .addTo(disposable)

    view.getActivityType()
        .map {
          it.request
        }
        .switchMap {
          repository.getAllLookUp(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderActivityType(it)
        }
        .addTo(disposable)

    view.getRepeatMode()
        .map {
          it.request
        }
        .switchMap {
          repository.getAllLookUp(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderRepeatMode(it)
        }
        .addTo(disposable)

    view.getWeekDays()
        .map {
          it.request
        }
        .switchMap {
          repository.getAllLookUp(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderWeekDays(it)
        }
        .addTo(disposable)

    view.getWeeks()
        .map {
          it.request
        }
        .switchMap {
          repository.getAllLookUp(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderWeeks(it)
        }
        .addTo(disposable)

    view.saveAppointmentMeeting()
        .map {
          it.request
        }
        .switchMap {
          repository.setAppointmentMeeting(it)
              .compose(applyUiModel())
        }
        .subscribe {
          view.renderAddUpdates(it)
        }
        .addTo(disposable)
  }
}