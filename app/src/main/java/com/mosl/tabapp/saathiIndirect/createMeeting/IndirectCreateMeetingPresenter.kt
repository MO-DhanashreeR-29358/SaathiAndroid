package com.mosl.tabapp.saathiIndirect.createMeeting

import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.domain.saathiIndirect.createMeeting.IndirectCreateMeetingRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants
import com.mosl.tabapp.model.CreateMeetingPostReq
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IndirectCreateMeetingPresenter @Inject constructor(
    view:IndirectCreateMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectrepo: IndirectSetAppointmentRepository,
    val indirectrepoCreateMeeting:IndirectCreateMeetingRepository,
    val repository: IndirectLeadDetailRepository,
    val validator: IndirectCreateMeetingValidator
) :BasePresenter<IndirectCreateMeetingView>(view){
    override fun start() {
        onFetchCalled().map{
            view.getLeadId()
        }.switchMap {
            repository.getLeadDetailForMainPage(it)
                .compose(applyUiModel())
        }.subscribe {
            view.renderLeadDetail(it)
        }

      view.ononActivityCall().switchMap {
            indirectrepo.getActivitytype().compose(applyUiModel())
        }.subscribe {
            view.fnRenderActivityType(it)
        }.addTo(disposable)


        val activityTypeSelectedObs = view.onActivityTypeSelected()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedActivityType()!!}
            .share()

        activityTypeSelectedObs.map {
            Timber.d("activity type")
            it?.isPersonalMeeting() ?: false
        }
            .startWithItem(false)
            .subscribe(view::toggleOTPVisibility)
            .addTo(disposable)

        activityTypeSelectedObs.filter { it != CreateMeetingConstants.SELECT_ACTIVITY_TYPE }.switchMap {
            indirectrepo.getMeetingReason(it.lookUpID).compose(applyUiModel())
        }.subscribe {
            view.fnRenderMeetingReason(it)
        }.addTo(disposable)

        view.onIntrestedInCall().switchMap {
            indirectrepoCreateMeeting.getInteresedIn(view.getInterestedInLookUpId()).compose(applyUiModel())
        }.subscribe { view.fnRenderIntrestedIn(it) }.addTo(disposable)

        view.onMeetingTypeCall()
            .switchMap {
                indirectrepoCreateMeeting.getMeetingType().compose(applyUiModel())
            }.subscribe {
                view.fnRenderMeetingType(it)
            }.addTo(disposable)


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

     /*   view.onSaveClicked()
            .map { view.getInputData() }
            .flatMap { input ->
                indirectrepoCreateMeeting.saveRequest(Gson().toJson(input))
            }
            .compose(applyUiModel())
            .subscribe { result ->
                view.renderSaveRequest(result)
            }
            .addTo(disposable)*/


        view.onIndirectLocationSaveEventFired()
            .subscribe {
                view.renderLocationSave(it.data)
            }
            .addTo(disposable)

        view.onResetClicked().subscribe {
            view.renderOnResetClicked()
        }.addTo(disposable)

        view.onAttendeeAddedClicked()
            .subscribe { view.showAttendeAddedDialog() }
            .addTo(disposable)

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
    }

    fun saveRequest(inputData: CreateMeetingPostReq) {
        indirectrepoCreateMeeting.saveRequest(Gson().toJson(inputData))
            .compose(applyUiModel())
            .subscribe { result ->
                view.renderSaveRequest(result)
            }
            .addTo(disposable)
    }
}