package com.mosl.tabapp.advancesearch


import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class QuickSearchTabPresenter @Inject constructor(
    view: QuickSearchTabView,
    val navigator: LeadViewDetailNavigator,
    val navigatorScreen: ScreenNavigatorImpl,
    val userManager: UserManager,
    val repo:AdvanceSearchRepository,
    val validator: QuickSearchTabValidator
): BasePresenter<QuickSearchTabView>(view) {
    override fun start() {

        onFetchCalled().map {
            view.fnGetSearchInput()
        }.flatMap {
                input->validator.validate(input)
            .flatMap {
                repo.fnGetAdvanceSearch(it)
            }.compose(applyUiModel())
        }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        view.onTableRowClicked().subscribe {
            view.fnRenderTableRowClicked(it.strLeadId,it.strUcId)
        } .addTo(disposable)

        view.onSearchButtonClicked().subscribe {
            view.fnRenderSearchButtonClicked()
        }.addTo(disposable)

        view.onResetButtonClicked().subscribe {
            view.fnRenderResetButtonClicked()
        }.addTo(disposable)

        view.onSetAppointmentClick()
            .subscribe { navigator.navigateToSetAppointment(view.getLeadId(), view.getUcid()) }
            .addTo(disposable)

        val createMeetingObs = view.onCreateMeetingClicked()
            .map { it.id }
            .share()
        createMeetingObs
            .filter { it == 0 }
            .subscribe { navigator.navigateToCreateMeeting(view.getLeadId(), view.getUcid()) }
            .addTo(disposable)

        view.onViewDetailsClicked().subscribe {
            val bundle = Bundle()
            bundle.putString(
                MOSLConstants.LEAD_ID,
                view.getLeadId()
            )
            bundle.putString(MOSLConstants.UCID,   view.getUcid())
            navigatorScreen.openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER.toString(), bundle, true, false)
        }.addTo(disposable)

    }
}