package com.mosl.tabapp.saathiIndirect.setAppointment

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit
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

        view.onAttendeeClicked()
            .subscribe {
                view.showAttendeDialog()
            }
            .addTo(disposable)


        val searchObs = view.getAttendeeSearchText()
            .filter { it.text.isNotEmpty() }
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .map { it.text }
            .distinctUntilChanged()
            .share()

        searchObs?.map(MapStringIsBlank)
            ?.subscribe {
                view.toggleSearchState(it, true)
            }
            ?.addTo(disposable)

        searchObs?.filter { it.length > 2 }
            ?.switchMap {
                indirectrepo.getAttendeeData(it)
                    .compose(applyUiModel())
            }
            ?.subscribe {
                view.renderSearchResult(it, true)
            }
            ?.addTo(disposable)

        view.onAttendeeItemClickEventFired()
            .map {
                it.position
            }
            .subscribe(view::renderOnAttendeeItemClick)
            .addTo(disposable)

        view.onSplAttendeeClicked()
            .subscribe {
                view.showSplAttendeDialog()
            }
            .addTo(disposable)

        view.onSplAttendeeAddedClicked()
            .subscribe { view.showSplAttendeAddedDialog() }
            .addTo(disposable)


        val searchSplAttendeeObs = view.getSplAttendeeSearchText()
            .filter { it.text.isNotEmpty() }
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .map { it.text }
            .distinctUntilChanged()
            .share()

        searchSplAttendeeObs?.map(MapStringIsBlank)
            ?.subscribe {
                view.toggleSearchState(it, false)
            }
            ?.addTo(disposable)

        searchSplAttendeeObs?.filter { it.length > 2 }
            ?.switchMap {
                indirectrepo.getSplAttendeeData(it)
                    .compose(applyUiModel())
            }
            ?.subscribe { view.renderSearchResult(it, false) }
            ?.addTo(disposable)

        view.onSplAttendeeItemClickEventFired()
            .map {
                it.position
            }
            .subscribe(view::renderOnSplAttendeeItemClick)
            .addTo(disposable)
    }



}