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
}