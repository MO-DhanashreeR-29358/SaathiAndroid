package com.mosl.tabapp.saathiIndirect.setAppointment

import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectSetAppointmentPresenter @Inject constructor(
    view:IndirectSetAppointmentView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectrepo: IndirectSetAppointmentRepository
):BasePresenter<IndirectSetAppointmentView>(view) {
    override fun start() {

        onFetchCalled().switchMap {
            indirectrepo.getMeetingReason(2820).compose(applyUiModel())
        }.subscribe {
            it.onFailure {
                Timber.d("ActivityResponse Error"+it.message)
            }
            it.onSuccess {
                Timber.d("ActivityResponse "+Gson().toJson(it))
            }

        }.addTo(disposable)


    }
}