package com.mosl.tabapp.leadcreation

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import io.reactivex.Observable
import javax.inject.Inject

class LeadCreationPresenter @Inject constructor(
  view: LeadCreationView
) : BasePresenter<LeadCreationView>(view) {

  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .subscribe()
        .addTo(disposable)

    view.onPrimaryCheckboxChanged()
        .subscribe(view::renderOnPrimaryCheckboxChanges)
        .addTo(disposable)

    view.onOfficePrimaryCheckboxChanged()
        .subscribe(view::renderOnOfficePrimaryCheckboxChanges)
        .addTo(disposable)

    view.onPrimaryInfoClicked()
        .subscribe { view.renderPrimaryInfoClick() }
        .addTo(disposable)

    view.onAdditionalInfoClicked()
        .subscribe { view.renderAdditionalInfoClick() }
        .addTo(disposable)

  }
}