package com.mosl.tabapp.saathiIndirect.setAppointment

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
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
           indirectrepo.getActivitytype().compose(applyUiModel())
        }.subscribe {
           view.fnRenderActivityType(it)
        }.addTo(disposable)


        val activityTypeSelectedObs = view.onActivityTypeSelected()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedActivityType() }
            .share()

        activityTypeSelectedObs.switchMap {
            indirectrepo.getMeetingReason(it.lookUpID).compose(applyUiModel())
        }.subscribe {
                view.fnRenderMeetingReason(it)
        }

        view.onDatePicker()
            .subscribe {
                view.renderDatePicker()
            }
            .addTo(disposable)



    }
}