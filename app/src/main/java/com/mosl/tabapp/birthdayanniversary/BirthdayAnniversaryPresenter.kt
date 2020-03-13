package com.mosl.tabapp.birthdayanniversary

import android.content.Context
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.RequestInput
import com.mosl.mobile.domain.birthdayanniversary.SendEmailRequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.BELATED_FLAG
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_A
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_B
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_BIRTHDAY
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_MSG_TYPE
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_TYPE_L
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_TYPE_T
import com.mosl.tabapp.constants.MOSLConstants
import io.reactivex.Observable

import javax.inject.Inject

class BirthdayAnniversaryPresenter @Inject constructor(
  view: BirthdayAnniversaryView,
  private val repository: BirthdayAnniversaryRepository,
  val context: Context,
  val userManager: UserManagerImpl
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
        getEmailRequest(it.selectedUcids, it.checkAll)
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

  private fun getEmailRequest(ucid: String?, isCheckAll: Boolean): SendEmailRequestInput {
    val eventType = if (view.getEventType().equals(KEY_BIRTHDAY, ignoreCase = true)) {
      KEY_B
    } else {
      KEY_A
    }
    val dayTypes = if (view.getDayType().equals(BELATED_FLAG, ignoreCase = true)) {
      KEY_TYPE_L
    } else {
      KEY_TYPE_T
    }


    return SendEmailRequestInput(
      selectedUcids = ucid,
      eventType = eventType + dayTypes,
      checkAll = isCheckAll,
      employeeCode = userManager.getUser()?.employeeCode,
      filterType = view.getFilterType(),
      messageType = KEY_MSG_TYPE
    )

  }

  private fun getProfileRequestInput(): LookupRequestInput {
    return LookupRequestInput(
      businessUnitId = userManager.getUser()?.currentBusinessProfile?.businessUnitId ?: "",
      lookupTypeId = MOSLConstants.LEADPROFILE,
      parentId = null,
      IgnoreBusinessUnit = null
    )
  }

  private fun getRequest(): RequestInput {
    val eventType = if (view.getEventType().equals(KEY_BIRTHDAY, ignoreCase = true)) {
      KEY_B
    } else {
      KEY_A
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