package com.mosl.tabapp.leadcreation.ui

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class LeadCreationPresenter @Inject constructor(
  view: LeadCreationView,
  val repository: LeadCreationRepository,
  val navigator: LeadCreationNavigator
) : BasePresenter<LeadCreationView>(view) {
  private val validator = LeadCreationValidator()

  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getLeadCreationPageData()
              .compose(applyUiModel())
        }
        .subscribe(view::renderLeadCreationPageData)
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

    view.onInterestedProductSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedInterestedProduct() }
        .switchMap {
          repository.getProductTypeRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderProductTypeOnInterestedProductSelection)
        .addTo(disposable)

    view.onCountrySelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedCountry() }
        .switchMap {
          repository.getStateRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderStateOnCountrySelection, Timber::e)
        .addTo(disposable)

    view.onStateSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedState() }
        .switchMap {
          repository.getCityRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderCityOnStateSelection)
        .addTo(disposable)

    view.onAdditionalInfoClicked()
        .subscribe { view.renderAdditionalInfoClick() }
        .addTo(disposable)

    view.onCreateButtonClicked()
        .map { view.getLeadCreationUserInput() }
        .switchMap {
          validator.validate(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOnCreateButtonClick)
        .addTo(disposable)

    view.onCancelButtonClicked()
        .subscribe { navigator.navigateToHome() }
        .addTo(disposable)

    view.onCreateLeadEventFired()
        .map { view.getLeadCreationUserInput() }
        .switchMap { input ->
          repository.createLead(input)
              .compose(applyUiModel())
        }
        .subscribe(view::renderCreateLead)
        .addTo(disposable)

  }
}