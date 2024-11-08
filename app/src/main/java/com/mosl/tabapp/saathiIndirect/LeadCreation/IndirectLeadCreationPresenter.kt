package com.mosl.tabapp.saathiIndirect.LeadCreation

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadCreationRepository
import com.mosl.tabapp.leadcreation.ui.LeadCreationView.Companion.INVALID_SELECTION_KEY
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class IndirectLeadCreationPresenter @Inject constructor(
    view: IndirectLeadCreationView,
    val repository: IndirectLeadCreationRepository,
    /*  val navigator: LeadCreationNavigator,*/
    val bankRepository: BankRepository,
    val userManager: UserManagerImpl
) : BasePresenter<IndirectLeadCreationView>(view) {
    private val validator = IndirectLeadCreationValidator(userManager.getUser()!!)

    override fun start() {
        Observable.merge(onFetchCalled(), onRetryCalled())
            .switchMap {
                repository.getStageRowDatas()
                    .compose(applyUiModel())
            }
            .subscribe(view::renderStageList, Timber::e)
            .addTo(disposable)



        view.onPrimaryCheckboxChanged()
            .skip(1)
            .subscribe(view::renderOnPrimaryCheckboxChanges, Timber::e)
            .addTo(disposable)

        view.onOfficePrimaryCheckboxChanged()
            .subscribe(view::renderOnOfficePrimaryCheckboxChanges, Timber::e)
            .addTo(disposable)

        view.onSameAsCheckboxClicked()
            .subscribe { view.renderOnSameAsAboveCheckboxClick() }
            .addTo(disposable)

        view.onAddressLineOneChanged()
            .filter { view.isSameAsAboveChecked() }
            .subscribe(view::renderOnAddressLineOneChange, Timber::e)
            .addTo(disposable)

        view.onAddressLineTwoChanged()
            .filter { view.isSameAsAboveChecked() }
            .subscribe(view::renderOnAddressLineTwoChange, Timber::e)
            .addTo(disposable)

        view.onAddressLineThreeChanged()
            .filter { view.isSameAsAboveChecked() }
            .subscribe(view::renderOnAddressLineThreeChange, Timber::e)
            .addTo(disposable)

        view.onPincodeChanged()
            .filter { view.isSameAsAboveChecked() }
            .subscribe(view::renderOnPincodeChange, Timber::e)
            .addTo(disposable)

        view.onPrimaryInfoClicked()
            .subscribe { view.renderPrimaryInfoClick() }
            .addTo(disposable)


        view.onInterestedProductSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedInterestedProduct() }
            .filter { rowData -> rowData.key != INVALID_SELECTION_KEY && rowData.value != "Real Estate - Physical"
                    && !userManager.getUser()!!.isAllianceBusiness()}
            .switchMap {
                repository.getStageRowDatas()
                    .compose(applyUiModel())
            }
            .subscribe(view::renderStageList, Timber::e)
            .addTo(disposable)

        view.onSourceSelected()
            .filter(InvalidSelectionPredicate)
            //.skip(1)
            .map { view.getSelectedSource() }
            //.filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
            .switchMap {
                repository.getProspectSourceRowDatas(it.key)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderProspectSourceOnSourceSelection, Timber::e)
            .addTo(disposable)

        view.onCategoryChanged()
            .filter(InvalidSelectionPredicate)
            //.skip(1)
            .map { view.getSelectedCategory() }
            //.filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
            .map { it.showPotentialPopup }
            .subscribe(view::renderOnCategoryChange, Timber::e)
            .addTo(disposable)


        view.onStageSelected()
            .filter(InvalidSelectionPredicate)
            //.skip(1)
            .map { view.getSelectedStage() }
            //.filter { rowData -> rowData.key != INVALID_SELECTION_KEY }
            .switchMap {
                repository.getStatusRowDatas(it.key)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderStatusOnStageSelection, Timber::e)
            .addTo(disposable)

        val refereeCodeObs = view.onRefereeCodeChanged()
            .debounce(1000, MILLISECONDS, dispatchers.main)
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
            .debounce(1000, MILLISECONDS, dispatchers.main)
            .share()

        refereeNameObs
            .filter { it.length > 2 }
            .distinctUntilChanged()
            .filter {
                view.getRefereeCode()
                    .isBlank()
            }
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
            .map { view.getSelectedCountry() }
            .switchMap {
                repository.getStateRowDatas(it.key)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderStateOnCountrySelection, Timber::e)
            .addTo(disposable)

        view.onOfficeCountrySelected()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedOfficeCountry() }
            .filter { !view.isSameAsAboveChecked() }
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
            .subscribe(view::renderCityOnStateSelection, Timber::e)
            .addTo(disposable)

        view.onOfficeStateSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedOfficeState() }
            .filter { !view.isSameAsAboveChecked() && it.key != INVALID_SELECTION_KEY }
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
            .subscribe(view::renderOnCitySelectAndSameAsAboveChecked, Timber::e)
            .addTo(disposable)


        view.onAdditionalInfoClicked()
            .subscribe { view.renderAdditionalInfoClick() }
            .addTo(disposable)

        view.onCreateButtonClicked()
            .doOnNext { Timber.i("Create button click") }
            .map { view.getLeadCreationUserInput() }
            .switchMap {
                validator.validate(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderOnCreateButtonClick, Timber::e)
            .addTo(disposable)

        view.onCancelButtonClicked()
            .subscribe { /*navigator.navigateToHome()*/ }
            .addTo(disposable)

      /*  view.onCreateLeadEventFired()
            .map { view.getLeadCreationUserInput() }
            .switchMap { input ->
                repository.createLead(input)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderCreateLead, Timber::e)
            .addTo(disposable)*/

        view.onTransferLeadEventFired()
            .map { it.leadId to view.getMobile() }
            .switchMap {
                repository.transferLeadForMobile(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderTransferLead)
            .addTo(disposable)

        view.onTransferLeadPaymentFired()
            .subscribe {/* navigator.navigateToLeadViewPager(it.leadId)*/ }
            .addTo(disposable)

    }
}