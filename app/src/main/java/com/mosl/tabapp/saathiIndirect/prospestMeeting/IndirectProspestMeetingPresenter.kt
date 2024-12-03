package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.IndirectProspestMeetingRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class IndirectProspestMeetingPresenter @Inject constructor(
    view:IndirectProspestMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectProRepo:IndirectProspestMeetingRepository
):BasePresenter<IndirectProspestMeetingView>(view) {

    override fun start() {
        view.onTextSearchCalled().filter { it.text.length > 2 }.switchMap {
            indirectProRepo.fnSearchProspectMeetingClient(it.text,null,null).compose(applyUiModel())
        }.subscribe {
           view.onTextSearchedRender(it)
        }.addTo(disposable)

        view.onFCodeTextSearchCalled().filter { it.text.length > 2 }.switchMap {
            indirectProRepo.fnSearchProspectMeetingClient(null,it.text,null).compose(applyUiModel())
        }.subscribe {
            view.onFCodeTextSearchRender(it)
        }.addTo(disposable)


        view.onSearchButtonClicked().subscribe {
            view.onSearchButtonRender()
        }

        view.onResetButtonClicked().subscribe {
            view.onResetButtonRender()
        }

    }
}