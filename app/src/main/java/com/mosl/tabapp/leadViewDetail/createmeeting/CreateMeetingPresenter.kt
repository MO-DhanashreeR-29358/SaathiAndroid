package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import com.mosl.mobile.domain.createmeeting.OTPRequestData
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragmentNew.Companion.SEND_OTP_LABEL
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragmentNew.Companion.VERIFY_OTP_LABEL
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
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

    val meetingLocationObs = view.onMeetingLocationSelected()
        .filter(InvalidSelectionPredicate)
        .map { t -> view.getSelectedMeetingLocation() }
        .share()

    meetingLocationObs.filter { !it.equals(CreateMeetingConstants.SELECT_MEETING_LOCATION) }
        .map { it.isLocationProper() }
//    meetingLocationObs.map {
//      Timber.d("location %s", it.isLocationProper())
//      it.isLocationProper()/*  && !it.equals(CreateMeetingConstants.SELECT_MEETING_LOCATION)*/
//    }
//        .filter {
//          !it.equals(
//              CreateMeetingConstants.SELECT_MEETING_LOCATION)
////              && !it.lookUpName.equals("Other", ignoreCase = false)
//        }
        .switchMap {
          repository.getAddress(
              view.getSelectedMeetingLocation()?.lookUpID.toString(), createMeetingData.leadId
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderAddress)
        .addTo(disposable)

//    view.onMeetingReasonSelected()
//        .filter(InvalidSelectionPredicate)
////        .map { t -> view.getSelectedMeetingReason() }
    val meetingReasonObs = view.onMeetingReasonSelected()
        .filter(InvalidSelectionPredicate)
        .map { t -> view.getSelectedMeetingReason() }
        .share()

    meetingReasonObs.map {
      Timber.d("customer service ")
      it.isCustomerService() && !it.equals(CreateMeetingConstants.SELECT_MEETING_REASON)
    }
        .startWith(false)
        .subscribe(view::toggleActionableVisibility)
        .addTo(disposable)

    meetingReasonObs.map { it.isCustomerService() }
        .switchMap {
          repository.getCustomerServiceData()
              .compose(applyUiModel())
        }
        .subscribe(view::renderCustomerServiceData)
        .addTo(disposable)

    view.onCategorySelected()
        .filter(InvalidSelectionPredicate)
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

    /*val searchObs = view.getSearchText()
        .filter { t -> t.isNotEmpty() && t.isNotBlank() }
        .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
        .map(CharSequenceToString)
        .distinctUntilChanged()
        .share()

    searchObs?.map(MapStringIsBlank)
        ?.subscribe(view::toggleSearchState)
        ?.addTo(disposable)

    searchObs?.filter { it.length > 2 }
        ?.switchMap {
          repository.getAttendeeData(it)
              .compose(applyUiModel())
        }
        ?.subscribe(view::renderSearchResult)
        ?.addTo(disposable)*/

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

    /*val otpClickObs = view.onSendOTPClicked()
        .map {
          view.getOTPButtonText()
        }.share()

    otpClickObs.filter { t -> t.equals(SEND_OTP_LABEL) }.subscribe{view.checkPermission()}
        .addTo(disposable)

    otpClickObs.filter { t -> t.equals(VERIFY_OTP_LABEL) }.subscribe{
      view.showOTPDialog(createMeetingData.ucid, createMeetingData.leadId)
    }.addTo(disposable)*/

    view.getOTPSubject()
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
