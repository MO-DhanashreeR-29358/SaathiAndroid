package com.mosl.tabapp.leadcreation.ui

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import com.mosl.tabapp.leadcreation.ui.LeadCreationView.Companion.INVALID_SELECTION_KEY
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
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

    view.onSameAsCheckboxClicked()
        .subscribe { view.renderOnSameAsAboveCheckboxClick() }
        .addTo(disposable)

    view.onAddressLineOneChanged()
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnAddressLineOneChange)
        .addTo(disposable)

    view.onAddressLineTwoChanged()
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnAddressLineTwoChange)
        .addTo(disposable)

    view.onAddressLineThreeChanged()
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnAddressLineThreeChange)
        .addTo(disposable)

    view.onPincodeChanged()
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnPincodeChange)
        .addTo(disposable)

    view.onPrimaryInfoClicked()
        .subscribe { view.renderPrimaryInfoClick() }
        .addTo(disposable)

    view.onInterestedProductSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedInterestedProduct() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
        .switchMap {
          repository.getProductTypeRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderProductTypeOnInterestedProductSelection)
        .addTo(disposable)

    view.onSourceSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedSource() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
        .switchMap {
          repository.getProspectSourceRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderProspectSourceOnSourceSelection)
        .addTo(disposable)


    view.onStageSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedStage() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
        .switchMap {
          repository.getStatusRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderStatusOnStageSelection)
        .addTo(disposable)

    view.onAssignToSearchEventFired()
        .debounce(1000, MILLISECONDS)
        .map { it.searchText }
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .switchMap {
          repository.getEmployeeModelsFilterBy(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOnAssignToSearchEventFire)
        .addTo(disposable)

    val refereeCodeObs = view.onRefereeCodeChanged()
        .debounce(1000, MILLISECONDS)
        .share()

    refereeCodeObs
        .filter { it.length > 3 }
        .distinctUntilChanged()
        .switchMap {
          repository.getRefereeCode(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOnRefereeCodeChange, Timber::e)
        .addTo(disposable)

    refereeCodeObs
        .filter { it.length == 3 }
        .distinctUntilChanged()
        .subscribe { view.resetRefereeOnCodeChange() }
        .addTo(disposable)

    val refereeNameObs = view.onRefereeNameChanged()
        .debounce(1000, MILLISECONDS)
        .share()

    refereeNameObs
        .filter { it.length > 2 }
        .distinctUntilChanged()
        .switchMap {
          repository.getRefereeName(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOnRefereeNameChange, Timber::e)
        .addTo(disposable)

    refereeNameObs
        .filter { it.length == 2 }
        .distinctUntilChanged()
        .subscribe { view.resetRefereeOnNameChange() }
        .addTo(disposable)

    view.onCountrySelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedCountry() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
        .switchMap {
          repository.getStateRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderStateOnCountrySelection, Timber::e)
        .addTo(disposable)

    view.onOfficeCountrySelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedOfficeCountry() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY && !view.isSameAsAboveChecked() }
        .switchMap {
          repository.getStateRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOfficeStateOnOfficeCountrySelection, Timber::e)
        .addTo(disposable)

    view.onStateSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedState() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
        .switchMap {
          repository.getCityRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderCityOnStateSelection)
        .addTo(disposable)

    view.onOfficeStateSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedOfficeState() }
        .filter { rowData -> rowData.key != INVALID_SELECTION_KEY && !view.isSameAsAboveChecked() }
        .switchMap {
          repository.getCityRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOfficeCityOnOfficeStateSelection, Timber::e)
        .addTo(disposable)

    view.onCitySelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnCitySelectAndSameAsAboveChecked)
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