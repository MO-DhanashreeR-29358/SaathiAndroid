package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.cncadmin

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class CncAdminDashboardPresenter @Inject constructor(
view:CncAdminDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: CncFocoRepository
):BasePresenter<CncAdminDashboardView>(view) {
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetCncDashboardFilter().compose(applyUiModel())
        }.subscribe {
           it.onSuccess {
               Log.d("cnc success",it)
           }
            it.onFailure {
                Log.d("cnc fail",it.message.toString())
            }
        }.addTo(disposable)

    }
}