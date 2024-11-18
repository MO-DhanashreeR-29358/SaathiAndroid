package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import com.mosl.app.core.base.BasePresenter
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailNavigator
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetailRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectLeadDetailMainPresenter @Inject constructor(
    view: IndirectLeadDetailMainView,
    val repository: IndirectLeadDetailRepository,
    val navigator: LeadViewDetailNavigator
) : BasePresenter<IndirectLeadDetailMainView>(view) {
    override fun start() {
        val fetchRetryObs = Observable.merge(onFetchCalled(), view.onRetryClick())
            .map { view.getUcid() }

        fetchRetryObs
            .map { view.getLeadId()}
            .switchMap {
                repository.getLeadDetailForMainPage(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderLeadDetail, Timber::e)
            .addTo(disposable)

        view.onThreeVerticalDotsClicked()
            .subscribe { view.showLeadViewDialog() }
            .addTo(disposable)

        val createMeetingObs = view.onCreateMeetingClicked()
            .map { it.id }
            .share()
        createMeetingObs
            .filter { it == 0 }
            .subscribe { navigator.navigateToCreateMeeting(view.getLeadId(), view.getUcid()) }
            .addTo(disposable)

        createMeetingObs
            .filter { it > 0 }
            .subscribe(view::renderCreateMeetingClick)
            .addTo(disposable)

        view.onSetAppointmentClick()
            .subscribe { navigator.navigateToSetAppointment(view.getLeadId(), view.getUcid()) }
            .addTo(disposable)

        view.onCallClicked()
            .subscribe { view.renderCallClick() }
            .addTo(disposable)

        view.onSMSClicked()
            .subscribe { view.renderSMSClick() }
            .addTo(disposable)
    }
}