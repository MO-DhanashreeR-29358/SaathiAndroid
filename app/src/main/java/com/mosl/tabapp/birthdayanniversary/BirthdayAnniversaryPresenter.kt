package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.RequestInput
import com.mosl.mobile.domain.birthdayanniversary.SendEmailRequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendEmailAll
import com.mosl.tabapp.constants.MOSLConstants
import io.reactivex.Observable
import javax.inject.Inject

class BirthdayAnniversaryPresenter @Inject constructor(
  view: BirthdayAnniversaryView,
  private val repository: BirthdayAnniversaryRepository
) : BasePresenter<BirthdayAnniversaryView>(view) {
  override fun start() {
    onFetchCalled()
      .map {
        getProfileRequestInput()
      }
      .switchMap {
        repository.getAllLookUp(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderProfileSpinner(it)
      }.addTo(disposable)

    view.getDataList()
      .map {
        BirthdayRequestInput(
          filter = it.filter,
          request = getRequest(),
          isAdminMode = it.isAdminMode
        )

      }
      .switchMap {
        repository.postBirthdayAnniversary(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderBirthdayAnniversary(it)
      }
      .addTo(disposable)



    view.onEmailAllClick()
      .subscribe {
        view.renderEmailAllClick()
      }.addTo(disposable)

    view.sendEmailAll()
      .map {
        it.getEmailRequest()
      }
      .switchMap {
        repository.postSendEmail(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderSendEmailAll(it)
      }
      .addTo(disposable)

    view.sendGift()
      .switchMap {
        repository.postSendGift(it.ucid, it.eventType)
          .compose(applyUiModel())
      }
      .subscribe {
        view.renderSendGift(it)
      }
      .addTo(disposable)

    val onSpinnerClick = Observable.merge(
      view.onProfileSpinnerSelection(),
      view.onEventTypeSpinnerSelection()
    ).filter(InvalidSelectionPredicate)
      .skip(1)

    val onButtonClick = Observable.merge(
      view.onButtonUpcomingClick(),
      view.onButtonBelatedClick(),
      view.onButtonTodayClick()
    )

    Observable.merge(onButtonClick, onSpinnerClick)
      .subscribe {
        view.renderGetData()
      }
      .addTo(disposable)

  }

  private fun SendEmailAll.getEmailRequest(): SendEmailRequestInput {
    return SendEmailRequestInput(
      selectedUcids = selectedUcids,
      eventType = eventType,
      checkAll = checkAll,
      employeeCode = employeeCode,
      filterType = filterType,
      messageType = messageType
    )

  }

  private fun getProfileRequestInput(): LookupRequestInput {
    return LookupRequestInput(
      businessUnitId = view.getBusinessId(),
      lookupTypeId = MOSLConstants.LEADPROFILE,
      parentId = null,
      IgnoreBusinessUnit = null
    )
  }

  private fun getRequest(): RequestInput {
    val eventType = if (view.getEventType().equals("Birthday", ignoreCase = true)) {
      "B"
    } else {
      "A"
    }
    return RequestInput(
      dayType = view.getFilterType(),
      totalCount = "0",
      pageSize = MOSLConstants.REPORT_PAGE_SIZE,
      occasion = eventType,
      calculatedPage = "0",
      page = "1"
    )
  }

}