package com.mosl.tabapp.saathiIndirect.setAppointment

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IndirectSetAppointmentPresenter @Inject constructor(
    view:IndirectSetAppointmentView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectrepo: IndirectSetAppointmentRepository,
    val repository: IndirectLeadDetailRepository,
    val validator: IndirectSetAppointmentValidator
):BasePresenter<IndirectSetAppointmentView>(view) {
    override fun start() {

        onFetchCalled().map{
            view.getLeadId()
        }.switchMap {
            repository.getLeadDetailForMainPage(it)
                .compose(applyUiModel())
        }.subscribe {
            view.renderLeadDetail(it)
        }.addTo(disposable)


        view.ononActivityCall().switchMap {
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
        }.addTo(disposable)

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

        view.onSaveButtonClicked().map {
            view.getSaveData()
        }.flatMap {
                    input->validator.validate(input)
            .flatMap {
                indirectrepo.setAppointmentMeeting(Gson().toJson(it))
            }.compose(applyUiModel())
        }.subscribe {
                view.renderAddUpdates(it)
            }.addTo(disposable)


        view.onResetButtonClicked().subscribe {
            view.onResentButtonRender()
        }.addTo(disposable)

        view.getMeetingById()
            .map {
                it.meetingId
            }
            .switchMap {
                indirectrepo.getMeetingById(it)
                    .compose(applyUiModel())
            }
            .subscribe {
                view.renderMeetingByID(it)
            }
            .addTo(disposable)


        view.onAttendeeAddedClicked()
            .subscribe { view.showAttendeAddedDialog() }
            .addTo(disposable)
    }

}