package com.mosl.tabapp.saathiIndirect.createMeeting

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.domain.saathiIndirect.createMeeting.IndirectCreateMeetingRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IndirectCreateMeetingPresenter @Inject constructor(
    view:IndirectCreateMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectrepo: IndirectSetAppointmentRepository,
    val indirectrepoCreateMeeting:IndirectCreateMeetingRepository
) :BasePresenter<IndirectCreateMeetingView>(view){
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
        }.addTo(disposable)

        view.onIntrestedInCall().switchMap {
            indirectrepoCreateMeeting.getInteresedIn().compose(applyUiModel())
        }.subscribe { view.fnRenderIntrestedIn(it) }.addTo(disposable)

        view.onMeetingDurationCall()
            .switchMap {
                indirectrepoCreateMeeting.getMeetingReason().compose(applyUiModel())
            }.subscribe {
              view.fnRenderMeetingDuration(it)
            }.addTo(disposable)

        view.onMeetingObjectiveCall().
                switchMap {
                    indirectrepoCreateMeeting.getMeetingObjective().compose(applyUiModel())
                }.subscribe {
                    view.fnRenderMeetingObjective(it)
        }.addTo(disposable)

        view.onLocationAddressCall().switchMap {
            indirectrepoCreateMeeting.getLocationAddress().compose(applyUiModel())
        }.subscribe {
            view.fnRenderLocationAddress(it)
        }.addTo(disposable)

        view.onTravellingModeCall().switchMap {
            indirectrepoCreateMeeting.getTravellingMode().compose(applyUiModel())
        }.subscribe { view.fnRenderTravellingMode(it) }.addTo(disposable)

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
                indirectrepoCreateMeeting.getAttendeeData(it)
                    .compose(applyUiModel())
            }
            ?.subscribe { view.renderSearchResult(it, true) }
            ?.addTo(disposable)

        view.onAttendeeItemClickEventFired()
            .map {
                it.position
            }
            .subscribe(view::renderOnAttendeeItemClick)
            .addTo(disposable)


        view.onMeetingLocationSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedMeetingLocation() }
            .filter {
                it != CreateMeetingConstants.SELECT_MEETING_LOCATION/* &&
                        it?.isLocationProper() ?: false*/
            }
            .switchMap {
                indirectrepoCreateMeeting.getAddress(it?.lookUpID.toString(), "")
                    .compose(applyUiModel())
            }
            .subscribe(view::renderAddress)
            .addTo(disposable)

        val travellingModeSelectedObs = view.onTravellingModeSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedTravellingMode() }
            .share()

        travellingModeSelectedObs.filter { it != CreateMeetingConstants.SELECT_TRAVELLING_MODE }
            .subscribe {
                view.getSelectedTravellingMode()
                    ?.let { it1 -> view.renderOnTravellingModeClick(it1) }
            }
            .addTo(disposable)


        view.onSaveClicked().
                map {
                    view.getInputData()
                }.switchMap {
                    indirectrepoCreateMeeting.saveRequest(it) .compose(applyUiModel())
        }.subscribe {
            it.onSuccess {
                Log.d("Create Meeting",Gson().toJson(it))
            }
            it.onFailure {
                Log.d("Create MeetingF",it.message.toString())
            }
        }.addTo(disposable)
    }
}