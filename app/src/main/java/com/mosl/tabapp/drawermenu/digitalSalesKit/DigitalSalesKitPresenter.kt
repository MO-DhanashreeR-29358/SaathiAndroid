package com.mosl.tabapp.drawermenu.digitalSalesKit

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportInput
import com.mosl.mobile.domain.report.HierarchyReportRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class DigitalSalesKitPresenter @Inject constructor(
  view: DigitalSalesKitView
) : BasePresenter<DigitalSalesKitView>(view) {
  override fun start() {

    view.onViewChange()
      .subscribe{view.renderViewChange()}
      .addTo(disposable)

    view.onItemClick()
      .subscribe {
        view.renderItemClick(it)
      }
  }


}