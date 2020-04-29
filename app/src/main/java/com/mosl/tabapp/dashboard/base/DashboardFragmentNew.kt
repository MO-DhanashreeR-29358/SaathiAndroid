package com.mosl.tabapp.dashboard.base

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragDashboardBinding
import javax.inject.Inject

class DashboardFragmentNew : CommonFragment(), DashboardView {
  internal lateinit var binding: FragDashboardBinding
  @Inject
  lateinit var presenter: DashboardPresenter

  override fun getLayout() = R.layout.frag_dashboard

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragDashboardBinding.bind(view)
    hideAppTitle()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  override fun render(uiModel: UiModel<List<DashboardData>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      for (dashboardData in it) {
        for (group in dashboardData.groups) {
          group.systemName
        }
      }
    }
  }

  companion object {
    const val ARG_PRODUCT_ID = "ProductID"
    const val ARG_PROFILE_ID = "ProfileID"
    const val ARG_EMPCODE = "EmployeeCode"
  }
}