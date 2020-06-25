package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailNavigator
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.ui.LeadCreationView
import com.mosl.tabapp.leaddetail.LeadDetail
import com.mosl.tabapp.leaddetail.LeadDetailAndProfession
import com.mosl.tabapp.leaddetail.LeadDetailRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class LeadDetailPresenter @Inject constructor(
  view: LeadDetailView,
  val data: LeadDetailArgument,
  val repository: LeadCreationRepository,
  val detailRepository: LeadDetailRepository,
  val navigator: LeadViewDetailNavigator
) : BasePresenter<LeadDetailView>(view) {
  override fun start() {
    val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .share()

    screenLoadObs
        .filter { //data.leadId == "0" &&
          data.ucId.toInt() >= 0
        }
        .switchMap {
          detailRepository.getClientDetailWithPortfolioAndProfitLoss(data.ucId)
              //detailRepository.getClientDetail(data.ucId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderClientDetailWithPortfolioAndProfitLoss, Timber::e)
        // .subscribe(view::renderClientDetail, Timber::e)
        .addTo(disposable)

    screenLoadObs
        .map { data.leadId }
        .filter { it != "0" }
        .switchMap {
          Observable.zip(
              repository.getProfessionRowDatas(),
              detailRepository.getLeadDetail(it),
              BiFunction { rows: List<RowData>, detail: LeadDetail ->
                LeadDetailAndProfession(rows, detail)
              }
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderLeadDetailAndProfession, Timber::e)
        .addTo(disposable)


    screenLoadObs
        .subscribe { view.renderArguments(data) }
        .addTo(disposable)

    screenLoadObs
        .switchMap {
          Observable.zip(
              repository.getProfileRowDatas(),
              repository.getInterestInRowDatas(),
              repository.getCountryRowDatas(),
              Function3 { listOfProfile: List<RowData>, listOfInterest: List<RowData>, listOfCountry: List<RowData> ->
                LeadDetailPageData(listOfProfile, listOfInterest, listOfCountry)
              }
          )
              .compose(applyUiModel())
        }
        .subscribe(view::render, Timber::e)
        .addTo(disposable)

    view.onUpdateProfileEventFired()
        .switchMap {
          repository.updateProfile(it.selectedProfile, data.ucId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderUpdateProfile, Timber::e)
        .addTo(disposable)

    view.onEditLeadDetailClicked()
        .subscribe { view.renderEditLeadDetailClick() }
        .addTo(disposable)
    view.onAnniversaryClicked()
        .subscribe { view.renderAnniversaryClick() }
        .addTo(disposable)

    view.onNestedScrollViewTouched()
        .subscribe { view.renderOnNestedScrollViewTouch() }
        .addTo(disposable)

    view.onClientDetailSectionClicked()
        .subscribe { view.toggleClientDetailSection() }
        .addTo(disposable)
    view.onClientPortfolioSectionClicked()
        .subscribe { view.toggleClientPortfolioSection() }
        .addTo(disposable)
    view.onLeadDetailSectionClicked()
        .subscribe { view.toggleLeadDetailSection() }
        .addTo(disposable)

    view.onPortfolioSnapshotClicked()
        .subscribe { view.togglePortfolio() }
        .addTo(disposable)
    view.onBrokingPortfolioClicked()
        .subscribe { view.toggleBrokingPortfolio() }
        .addTo(disposable)
    view.onAssetPortfolioClicked()
        .subscribe { view.toggleAssetPortfolio() }
        .addTo(disposable)
    view.onLedgerClicked()
        .subscribe { view.toggleLedger() }
        .addTo(disposable)
    view.onProfitLossSnapshotClicked()
        .subscribe { view.toggleProfitLoss() }
        .addTo(disposable)
    view.onBrokingProductClicked()
        .subscribe { view.toggleBrokingProduct() }
        .addTo(disposable)
    view.onAssetProductClicked()
        .subscribe { view.toggleAssetProduct() }
        .addTo(disposable)
    view.onAvailableLimitSnapshotClicked()
        .subscribe { view.toggleAvailableLimit() }
        .addTo(disposable)

    view.onDocumentClicked()
        .subscribe({ navigator.openDocument(data.leadId, data.ucId) }, {
          Timber.e(it)
        })
        .addTo(disposable)

    view.onRefereeCodeFocusChanged()
        .subscribe { view.renderOnRefereeCodeFocusChange() }
        .addTo(disposable)

    view.onRefereeNameFocusChanged()
        .subscribe { view.renderOnRefereeNameFocusChange() }
        .addTo(disposable)

    view.onLeadNameAfterTextChanged()
        .subscribe { view.renderOnLeadNameAfterTextChange(it) }
        .addTo(disposable)

    view.onProfileLayoutClicked()
        .subscribe { view.renderProfileClick() }
        .addTo(disposable)

    view.onProsRefereeNameAfterTextChanged()
        .subscribe { view.renderOnProsRefereeNameAfterTextChange(it) }
        .addTo(disposable)

    view.onProsRefereeMobileTextChanged()
        .subscribe { view.renderOnProsRefereeMobileTextChange(it) }
        .addTo(disposable)

    view.onProsRefereeEmailTextChanged()
        .subscribe { view.renderOnProsRefereeEmailTextChange(it) }
        .addTo(disposable)

    view.onContactPersonAfterTextChanged()
        .subscribe { view.renderOnContactPersonAfterTextChange(it) }
        .addTo(disposable)

    view.onLandLineAfterTextChanged()
        .subscribe { view.renderOnLandLineAfterTextChange(it) }
        .addTo(disposable)

    view.onAlterMobileTextChanged()
        .subscribe { view.renderOnAlterMobileTextChange(it) }
        .addTo(disposable)

    view.onAlterEmailTextChanged()
        .subscribe { view.renderOnAlterEmailTextChange(it) }
        .addTo(disposable)
    view.onEmailTextChanged()
        .subscribe { view.renderOnEmailTextChange(it) }
        .addTo(disposable)
    view.onPanTextChanged()
        .subscribe { view.renderOnPanTextChange(it) }
        .addTo(disposable)
    view.onAadharAfterTextChanged()
        .subscribe { view.renderOnAadharAfterTextChange(it) }
        .addTo(disposable)
    view.onPinTextChanged()
        .subscribe { view.renderOnPinTextChange(it) }
        .addTo(disposable)

    view.onOfficePinTextChanged()
        .subscribe { view.renderOnOfficePinTextChange(it) }
        .addTo(disposable)

    view.onAdd1AfterTextChanged()
        .filter { view.isCbSameAsChecked() }
        .subscribe { view.renderOnAdd1AfterTextChange(it) }
        .addTo(disposable)

    view.onAdd2AfterTextChanged()
        .filter { view.isCbSameAsChecked() }
        .subscribe { view.renderOnAdd2AfterTextChange(it) }
        .addTo(disposable)

    view.onAdd3AfterTextChanged()
        .filter { view.isCbSameAsChecked() }
        .subscribe { view.renderOnAdd3AfterTextChange(it) }
        .addTo(disposable)

    val refereeCodeObs = view.onRefereeCodeChanged()
        .debounce(1000, MILLISECONDS, dispatchers.main)
        .filter { view.isRefereeCodeFocused() }
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
        .filter { view.isRefereeNameFocused() }
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
        .map { view.getSelectedState() }
        .filter { rowData -> rowData.key != LeadCreationView.INVALID_SELECTION_KEY }
        .switchMap {
          repository.getCityRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderCityOnStateSelection, Timber::e)
        .addTo(disposable)

    view.onOfficeStateSelected()
        .filter(InvalidSelectionPredicate)
        .map { view.getSelectedOfficeState() }
        .filter { !view.isSameAsAboveChecked() }
        .switchMap {
          repository.getCityRowDatas(it.key)
              .compose(applyUiModel())
        }
        .subscribe(view::renderOfficeCityOnOfficeStateSelection, Timber::e)
        .addTo(disposable)

    view.onCitySelected()
        .filter(InvalidSelectionPredicate)
        .filter { view.isSameAsAboveChecked() }
        .subscribe(view::renderOnCitySelectAndSameAsAboveChecked, Timber::e)
        .addTo(disposable)

  }
}