package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import javax.inject.Inject

class BirthdayAnniversaryPresenter @Inject constructor(
  view: BirthdayAnniversaryView,
  private val repository: BirthdayAnniversaryRepository
) : BasePresenter<BirthdayAnniversaryView>(view) {
  override fun start() {
    onFetchCalled()
      .map {
        view.getProfileRequestInput()
      }
      .switchMap {
        repository.getAllLookUp(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderProfileSpinner(it)
      }.addTo(disposable)
  }

}