package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFABuisnessSnapshotIpPresenter @Inject constructor(
    view: IndirectIFABuisnessSnapshotIpView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
) :BasePresenter<IndirectIFABuisnessSnapshotIpView>(view){
    override fun start() {
        onFetchCalled().switchMap {
indirectRepo.fnGetIFASalesDashMyBuisnessSnapIP().compose(applyUiModel())
        }.subscribe {
            it.onFailure {
                Timber.d("ResponseError"+it.message)
            }
            it.onSuccess {
                Timber.d("ResponseIFA "+it)
            }
        }

    }
}