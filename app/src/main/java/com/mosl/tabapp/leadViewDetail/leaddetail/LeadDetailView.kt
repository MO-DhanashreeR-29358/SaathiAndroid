package com.mosl.tabapp.leadViewDetail.leaddetail

import android.view.MotionEvent
import com.mosl.app.core.base.BaseView
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import io.reactivex.Observable

interface LeadDetailView : BaseView {
  fun renderArguments(data: LeadDetailArgument)
  fun onNestedScrollViewTouched(): Observable<MotionEvent>
  fun renderOnNestedScrollViewTouch()
  fun onPrimaryAddCheckboxClicked(): Observable<Unit>
  fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit>
  fun onSameAsCheckboxClicked(): Observable<Unit>
  fun onSubmitButtonClicked(): Observable<Unit>
  fun onProfileLayoutClicked(): Observable<Unit>
  fun onAddProductClicked(): Observable<Unit>
  fun onEditLeadDetailClicked(): Observable<Unit>
  fun onClientDetailClicked(): Observable<Unit>
  fun onClientPortfolioDetailClicked(): Observable<Unit>
  fun onLeadDetailClicked(): Observable<Unit>
  fun onDocumentClicked(): Observable<Unit>
  fun onDOBClicked(): Observable<Unit>
  fun onAnniversaryClicked(): Observable<Unit>
  fun onRefereeCodeFocusChanged(): Observable<Boolean>
  fun renderOnRefereeCodeFocusChange()
  fun onRefereeNameFocusChanged(): Observable<Boolean>
  fun renderOnRefereeNameFocusChange()
  fun onLeadNameAfterTextChanged(): Observable<String>
  fun renderOnLeadNameAfterTextChange(text: String)
  fun onProsRefereeNameAfterTextChanged(): Observable<String>
  fun renderOnProsRefereeNameAfterTextChange(text: String)
  fun onProsRefereeMobileTextChanged(): Observable<String>
  fun renderOnProsRefereeMobileTextChange(text: String)
  fun onProsRefereeEmailTextChanged(): Observable<String>
  fun renderOnProsRefereeEmailTextChange(text: String)

  fun onContactPersonAfterTextChanged(): Observable<String>
  fun renderOnContactPersonAfterTextChange(text: String)
  fun onLandLineAfterTextChanged(): Observable<String>
  fun renderOnLandLineAfterTextChange(text: String)
  fun onAlterMobileTextChanged(): Observable<String>
  fun renderOnAlterMobileTextChange(text: String)
  fun onAlterEmailTextChanged(): Observable<String>
  fun renderOnAlterEmailTextChange(text: String)
  fun onEmailTextChanged(): Observable<String>
  fun renderOnEmailTextChange(text: String)
  fun onPanTextChanged(): Observable<String>
  fun renderOnPanTextChange(text: String)
  fun onAadharAfterTextChanged(): Observable<String>
  fun renderOnAadharAfterTextChange(text: String)
  fun onPinTextChanged(): Observable<String>
  fun renderOnPinTextChange(text: String)
  fun onOfficePinTextChanged(): Observable<String>
  fun renderOnOfficePinTextChange(text: String)
  fun onAdd1AfterTextChanged(): Observable<String>
  fun renderOnAdd1AfterTextChange(text: String)
  fun onAdd2AfterTextChanged(): Observable<String>
  fun renderOnAdd2AfterTextChange(text: String)
  fun onAdd3AfterTextChanged(): Observable<String>
  fun renderOnAdd3AfterTextChange(text: String)
  fun isCbSameAsChecked(): Boolean
}