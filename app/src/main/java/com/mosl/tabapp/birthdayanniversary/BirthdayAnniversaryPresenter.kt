package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
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

    view.getDataList()
      .map {
        it.request
      }
      .switchMap {
        repository.postBirthdayAnniversary(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderBirthdayAnniversary(it)
      }
      .addTo(disposable)

    view.onProfileSpinnerSelection()
      .filter(InvalidSelectionPredicate)
      .subscribe {
        view.renderGetData()
      }
      .addTo(disposable)

    view.onEventTypeSpinnerSelection()
      .filter(InvalidSelectionPredicate)
      .skip(1)
      .subscribe {
        view.renderGetData()
      }
      .addTo(disposable)

  }

}