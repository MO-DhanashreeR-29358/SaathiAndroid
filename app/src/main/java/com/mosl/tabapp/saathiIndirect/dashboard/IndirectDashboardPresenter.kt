package com.mosl.tabapp.saathiIndirect.dashboard

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.mobile.domain.saathiIndirect.dashboard.DashboardIndirctRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.*
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectDashboardPresenter @Inject constructor(
    view: IndirectDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: DashboardIndirctRepository
) : BasePresenter<IndirectDashboardView>(view) {
    override fun start() {
            onFetchCalled().switchMap{
                indirectRepo.fnGetFOCODashboardFilter().compose(applyUiModel())
            }.subscribe {
                it.onFailure {
                    Timber.d("Response Error"+it.message)
                }
                it.onSuccess {
                    Timber.d("Response "+it)
                }

            }.addTo(disposable)

        view.onLeadCreationClicked()
            .subscribe {
                navigator.openPage(NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_CREATION.toString(), false)
            }
            .addTo(disposable)
    }

}
