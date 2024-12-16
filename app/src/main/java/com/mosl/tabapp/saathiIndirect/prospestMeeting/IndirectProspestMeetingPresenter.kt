package com.mosl.tabapp.saathiIndirect.prospestMeeting

import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.IndirectProspestMeetingRepository
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.domain.saathiIndirect.createMeeting.IndirectCreateMeetingRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IndirectProspestMeetingPresenter @Inject constructor(
    view:IndirectProspestMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectProRepo:IndirectProspestMeetingRepository,
    val indirectrepoCreateMeeting: IndirectCreateMeetingRepository,
    val repository: IndirectLeadDetailRepository,
    val indirectrepo: IndirectSetAppointmentRepository,
    val validator: IndirectProspectMeetingValidation,
    val validatorSearch:IndirectProsspectMeetingsearchValiation
):BasePresenter<IndirectProspestMeetingView>(view) {

    override fun start() {
        view.onTextSearchCalled().filter { it.text.length > 2 }.switchMap {
            indirectProRepo.fnSearchProspectMeetingClient(it.text,null,null).compose(applyUiModel())
        }.subscribe {
           view.onTextSearchedRender(it)
        }.addTo(disposable)

        view.onFCodeTextSearchCalled().filter { it.text.length > 2 }.switchMap {
            indirectProRepo.fnSearchProspectMeetingClient(null,it.text,null).compose(applyUiModel())
        }.subscribe {
            view.onFCodeTextSearchRender(it)
        }.addTo(disposable)


        view.onSearchButtonClicked().map {
            view.getLeadId()
        }.flatMap {
            input->validatorSearch.validate(input).flatMap {
            repository.getLeadDetailForMainPage(it)
        } .compose(applyUiModel())
        }.subscribe {
            view.renderLeadDetail(it)
        }.addTo(disposable)

        view.onResetButtonClicked().subscribe {
            view.onResetButtonRender()
        }

        // create meeting


        view.ononActivityCall().switchMap {
            indirectrepo.getActivitytype().compose(applyUiModel())
        }.subscribe {
            view.fnRenderActivityType(it)
        }.addTo(disposable)


        val activityTypeSelectedObs = view.onActivityTypeSelected()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedActivityType() }
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


        view.onSaveClicked().map {
            view.getInputData()
        }.flatMap {
                input->validator.validate(input).flatMap {
            indirectrepoCreateMeeting.saveRequetProspectMeeting(Gson().toJson(it))
        }.compose(applyUiModel())
        }.subscribe {
            view.renderSaveRequest(it)
        }.addTo(disposable)

        view.onIndirectLocationSaveEventFired()
            .subscribe {
                view.renderLocationSave(it.data)
            }
            .addTo(disposable)

        view.onResetClicked().subscribe {
            view.renderOnResetClicked()
        }.addTo(disposable)

    }
}