package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import io.reactivex.Observable
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

    Observable.merge(
      view.onProfileSpinnerSelection(),
      view.onEventTypeSpinnerSelection()
    )
      .filter(InvalidSelectionPredicate)
      .skip(1)
      .subscribe {
        view.renderGetData()
      }
      .addTo(disposable)

    view.onEmailAllClick()
      .subscribe {
        view.renderEmailAllClick()
      }.addTo(disposable)

    view.sendEmailAll()
      .map {
        it.request
      }
      .switchMap {
        repository.postSendEmail(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderSendEmailAll(it)
      }
      .addTo(disposable)

    view.sendGift()
      .switchMap {
        repository.postSendGift(it.ucid, it.eventType)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderSendGift(it)
      }
      .addTo(disposable)

  }

}