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

    view.getClientDetail()
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
        view.renderLeadViews(it)
      }.addTo(disposable)

  }
}