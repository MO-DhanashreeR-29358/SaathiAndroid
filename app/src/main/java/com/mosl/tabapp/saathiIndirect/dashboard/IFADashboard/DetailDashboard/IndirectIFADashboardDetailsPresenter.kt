package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class IndirectIFADashboardDetailsPresenter @Inject constructor(
    view: IndirectIFADashboardDetailsView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFADashboardDetailsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)


    }


}