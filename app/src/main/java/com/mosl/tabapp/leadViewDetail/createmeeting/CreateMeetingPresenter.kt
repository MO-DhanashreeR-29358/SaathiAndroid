package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import com.mosl.mobile.domain.createmeeting.OTPRequestData
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragmentNew.Companion.SEND_OTP_LABEL
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragmentNew.Companion.VERIFY_OTP_LABEL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class CreateMeetingPresenter @Inject constructor(
  view: CreateMeetingView,
  val repository: CreateMeetingRepository,
  val createMeetingData: CreateMeetingData
) : BasePresenter<CreateMeetingView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getInitialPageData(createMeetingData)
              .compose(applyUiModel())
        }
        .subscribe(view::render)
        .addTo(disposable)

    view.isFranchiseIFAUser()
        .switchMap {
          repository.getFranchiseIFAUserData()
              .compose(applyUiModel())
        }
        .subscribe(view::renderFranchiseIFAUserData)
        .addTo(disposable)

    val activityTypeSelectedObs = view.onActivityTypeSelected()
        .filter(InvalidSelectionPredicate)
        .map { t -> view.getSelectedActivityType() }
        .share()

    activityTypeSelectedObs.map {
      Timber.d("activity type")
      it.isPersonalMeeting()
    }
        .startWith(false)
        .subscribe(view::toggleOTPVisibility)
        .addTo(disposable)

    activityTypeSelectedObs
        .filter { !it.equals(CreateMeetingConstants.SELECT_ACTIVITY_TYPE) }
        .switchMap {
          repository.getLookup(
              MEETINGREASON, null, view.getSelectedActivityType()?.lookUpID.toString()
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderMeetingReason)
        .addTo(disposable)


    view.onMeetingLocationSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedMeetingLocation() }
        .filter {
          it != CreateMeetingConstants.SELECT_MEETING_LOCATION &&
              it.isLocationProper()
        }
        .switchMap {
          repository.getAddress(it.lookUpID.toString(), createMeetingData.leadId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderAddress)
        .addTo(disposable)

    val meetingReasonObs = view.onMeetingReasonSelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { t -> view.getSelectedMeetingReason() }
        .filter { it != CreateMeetingConstants.SELECT_MEETING_REASON }
        .share()

    meetingReasonObs.map {
      it.isCustomerService()
    }
        .startWith(false)
        .subscribe(view::toggleActionableVisibility)
        .addTo(disposable)

    meetingReasonObs.filter { it.isCustomerService() }
        .switchMap {
          repository.getCustomerServiceData()
              .compose(applyUiModel())
        }
        .subscribe(view::renderCustomerServiceData)
        .addTo(disposable)

    view.onCategorySelected()
        .filter(InvalidSelectionPredicate)
        .skip(1)
        .map { view.getSelectedCategory() }
        .switchMap {
          repository.getLookup(SUBCATEGORY, null, it.lookUpID.toString())
              .compose(applyUiModel())
        }
        .subscribe(view::renderSubCategory)
        .addTo(disposable)

    view.onAttendeeClicked()
        .subscribe {
          view.showAttendeDialog()
        }
        .addTo(disposable)

    view.onSplAttendeeClicked()
        .subscribe {
          view.showSplAttendeDialog()
        }
        .addTo(disposable)

    val searchObs = view.getAttendeeSearchText()
        .filter { it.text.isNotEmpty() }
        .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
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
          repository.getAttendeeData(it)
              .compose(applyUiModel())
        }
        ?.subscribe { view.renderSearchResult(it, true) }
        ?.addTo(disposable)

    val searchSplAttendeeObs = view.getSplAttendeeSearchText()
        .filter { it.text.isNotEmpty() }
        .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
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
          repository.getSplAttendeeData(it)
              .compose(applyUiModel())
        }
        ?.subscribe { view.renderSearchResult(it, false) }
        ?.addTo(disposable)

    view.onAttendeeAddedClicked()
        .subscribe { view.showAttendeAddedDialog() }
        .addTo(disposable)

    view.onSplAttendeeAddedClicked()
        .subscribe { view.showSplAttendeAddedDialog() }
        .addTo(disposable)

    val isOTPSentSuccessful = view.onSendOTPClicked()
        .map {
          view.isOTPSuccessful()
        }
        .share()

    isOTPSentSuccessful.filter { t: Boolean ->
      t.equals(true)
    }
        .subscribe { view.showToast() }
        .addTo(disposable)

    isOTPSentSuccessful.filter { t: Boolean -> t.equals(false) }
        .map { view.getOTPButtonText() }
        .filter { t -> t.equals(SEND_OTP_LABEL) }
        .subscribe { view.checkPermissionAndProceed() }
        .addTo(disposable)

    isOTPSentSuccessful.filter { t: Boolean -> t.equals(false) }
        .map { view.getOTPButtonText() }
        .filter { t -> t.equals(VERIFY_OTP_LABEL) }
        .subscribe { view.showOTPDialog(createMeetingData.ucid, createMeetingData.leadId) }
        .addTo(disposable)

    view.onAttendeeItemClickEventFired()
        .map {
          it.position
        }
        .subscribe(view::renderOnAttendeeItemClick)
        .addTo(disposable)

    view.onSplAttendeeItemClickEventFired()
        .map {
          it.position
        }
        .subscribe(view::renderOnSplAttendeeItemClick)
        .addTo(disposable)

    view.sendOTP()
        .switchMap {
          repository.sendOTP(
              OTPRequestData(
                  latitude = view.getLatitude(),
                  longitude = view.getLongitude(),
                  geoArea = view.getGeoArea(),
                  ucid = createMeetingData.ucid,
                  leadid = createMeetingData.leadId
              )
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderOTP)
        .addTo(disposable)
  }

  companion object {
    const val MEETINGREASON = 14
    const val SUBCATEGORY = 22
  }

}
