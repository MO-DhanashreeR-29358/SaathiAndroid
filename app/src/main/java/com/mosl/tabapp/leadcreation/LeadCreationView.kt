package com.mosl.tabapp.leadcreation

import com.mosl.app.core.base.BaseView
import io.reactivex.Observable

interface LeadCreationView : BaseView {

  fun onPrimaryCheckboxChanged(): Observable<Boolean>
  fun renderOnPrimaryCheckboxChanges(isChecked: Boolean)

  fun onOfficePrimaryCheckboxChanged(): Observable<Boolean>
  fun renderOnOfficePrimaryCheckboxChanges(isChecked: Boolean)

  fun onPrimaryInfoClicked(): Observable<Unit>
  fun onAdditionalInfoClicked(): Observable<Unit>
  fun renderPrimaryInfoClick()
  fun renderAdditionalInfoClick()
  fun onCreateButtonClicked(): Observable<Unit>
  fun onCancelButtonClicked(): Observable<Unit>
  fun onSameAsCheckboxClicked(): Observable<Unit>
}