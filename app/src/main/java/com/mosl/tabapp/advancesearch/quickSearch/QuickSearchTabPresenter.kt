package com.mosl.tabapp.advancesearch.quickSearch

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class QuickSearchTabPresenter @Inject constructor(
    view: QuickSearchTabView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val repo:AdvanceSearchRepository
): BasePresenter<QuickSearchTabView>(view) {
    override fun start() {

        onFetchCalled().map {
            view.fnGetSearchInput()
        }.switchMap {
            repo.fnGetAdvanceSearch(it).compose(applyUiModel())
        }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        view.onTableRowClicked().subscribe {
            view.fnRenderTableRowClicked(it.strLeadId)
        } .addTo(disposable)

        view.onSearchButtonClicked().subscribe {
            view.fnRenderSearchButtonClicked()
        }.addTo(disposable)

        view.onResetButtonClicked().subscribe {
            view.fnRenderResetButtonClicked()
        }.addTo(disposable)
    }
}