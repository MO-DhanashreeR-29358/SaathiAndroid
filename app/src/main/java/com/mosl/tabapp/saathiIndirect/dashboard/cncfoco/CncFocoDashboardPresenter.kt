package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class CncFocoDashboardPresenter @Inject constructor(
    view:CncFocoDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo:CncFocoRepository
) :BasePresenter<CncFocoDashboardView>(view){
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetCncDashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)

    }
}