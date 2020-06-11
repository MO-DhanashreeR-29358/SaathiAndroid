package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadDetailPresenter @Inject constructor(
  view: LeadDetailView,
  val data: LeadDetailArgument
) : BasePresenter<LeadDetailView>(view) {
  override fun start() {
    val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .share()

    screenLoadObs
        .subscribe { view.renderArguments(data) }
        .addTo(disposable)

    view.onNestedScrollViewTouched()
        .subscribe { view.renderOnNestedScrollViewTouch() }
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


  }
}