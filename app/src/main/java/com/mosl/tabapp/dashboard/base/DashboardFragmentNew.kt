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
import com.mosl.tabapp.dashboard.clients.ClientsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.databinding.FragDashboardBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
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
    setupViews()
  }

  private fun setupViews() {
    if (userManager.getUser()!!.isAufsb()) {
      binding.layoutAufsb.lldashboardAufsb.visibility = View.VISIBLE
      binding.ivActionableInsights.visibility = View.GONE
      binding.tvMain.visibility = View.GONE
    } else {
      binding.layoutAufsb.lldashboardAufsb.visibility = View.GONE
    }
  }

  override fun render(uiModel: UiModel<List<DashboardData>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      for (dashboardData in it) {
        fillAvailableWidgetModel(dashboardData)
        renderDashboard(dashboardData)
        if (dashboardList.size > 0) {
          currentDashboard = dashboardList.get(0)
          currentDashboard.businessProfileIdForDashboard
        }
      }
    }
  }

  private fun renderDashboard(dashboardData: DashboardData) {
    for (group in dashboardData.groups) {
      val systemName = group.systemName
      if (systemName.equals(MyClient_MyLead)) {
        binding.llClient.visibility = View.VISIBLE
        loadClientsFragment()
      } else if (systemName.equals(MY_LEAD)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadsFragment()
      } else if (systemName.equals(MY_LEAD_AU_SUPPORT)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadsFragment()
      } else if (systemName.equals(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY)) {
        binding.llClient.visibility = View.VISIBLE
        loadClientsFragment()
      } else if (systemName.equals(SYS_LEAD_SNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadBusinessSnapshotFragment()
      } else if (systemName.equals(TEAM_LEAD)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadsFragment()
      } else if (systemName.equals(SYS_MY_CLIENTS_SNAPSHOOT)) {
        binding.llClient.visibility = View.VISIBLE
        loadClientsFragment()
      } else if (systemName.equals(SYS_TEAM_SNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadBusinessSnapshotFragment()
      } else if (systemName.equals(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        loadLeadBusinessSnapshotFragment()
      } else if (systemName.equals(SYS_BUSINESS_SNAPSHOT)) {
        binding.llSnapshot.visibility = View.VISIBLE
        loadLeadBusinessSnapshotFragment()
      } else if (systemName.equals(SYS_RH_BUSINESS_SNAPSHOT)) {
        binding.llSnapshot.visibility = View.VISIBLE
        loadLeadBusinessSnapshotFragment()
      } else if (systemName.equals(SELF_TARGET_ACHIEVEMNET)) {
        binding.llTarget.visibility = View.VISIBLE
        loadTargetAchievementFragment()
      } else if (systemName.equals(AUSFB_TARGET_ACHIEVEMNET)) {
        binding.llTarget.visibility = View.VISIBLE
        loadTargetAchievementFragment()
      } else if (systemName.equals(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT)) {
        binding.llTarget.visibility = View.VISIBLE
        loadTargetAchievementFragment()
      } else if (systemName.equals(MONTH_ON_MONTH)) {
        binding.llMonthTrend.visibility = View.VISIBLE
        loadMonthTrendsFragment()
      } else if (systemName.equals(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
        binding.llPerformanceAndIncentives.visibility = View.VISIBLE
      } else if (systemName.equals(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)
          || systemName.equals(MY_PERFORMANCE_INCENTIVE)
      ) {
        binding.llPerformanceAndIncentives.visibility = View.VISIBLE
      }
    }
  }

  val dashboardList: ArrayList<DashboardModel> = ArrayList()
  lateinit var currentDashboard: DashboardModel
  private fun fillAvailableWidgetModel(data: DashboardData) {
    val dashboardId = data.ID.toString()
    val dashboardName = data.name
    val dashboardFor = data.dashboardFor

    val dashboardModel = DashboardModel()
    dashboardList.add(dashboardModel)
    dashboardModel.id = dashboardId
    dashboardModel.name = dashboardName
    dashboardModel.dashboardFor = dashboardFor

    val groupsItems = data.groups

    val mainGroupList: List<DashboardGroupModel> =
      getGroupModelFromJsonArray(groupsItems)
    dashboardModel.groupList
        .clear()
    dashboardModel.groupList
        .addAll(mainGroupList)
  }

  private fun loadMonthTrendsFragment() {
    val monthOnMonthTrendsFragment = MonthOnMonthTrendsFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    monthOnMonthTrendsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llMonthvsTrends, monthOnMonthTrendsFragment)
        .commit()

  }

  private fun loadTargetAchievementFragment() {
    val leadsFragment = TargetFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llTargetAchievementADV, leadsFragment)
        .commit()
  }

  private fun loadLeadBusinessSnapshotFragment() {
    val leadsFragment = BusinessSnapshotFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()
  }

  private fun loadLeadsFragment() {
    val leadsFragment = LeadsFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()

  }

  val args = Bundle()
  private fun loadClientsFragment() {
    val clientsFragment = ClientsFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    clientsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, clientsFragment)
        .commit()
  }

  companion object {
    const val ARG_PRODUCT_ID = "ProductID"
    const val ARG_PROFILE_ID = "ProfileID"
    const val ARG_EMPCODE = "EmployeeCode"
    const val SYS_MY_CLIENTS_SNAPSHOOT = "My_Clients_Snapshot"
    const val SYS_LEAD_SNAPSHOT = "LeadSnapshot"
    const val SYS_TEAM_SNAPSHOT = "Team_Snapshot"
    const val SYS_BUSINESS_SNAPSHOT = "BusinessSnapshot"
    const val SYS_RH_BUSINESS_SNAPSHOT = "RH_BusinessSnapshot"
    const val MY_LEAD = "My_Lead"
    const val MY_LEAD_AU_SUPPORT = "My_Lead_AU_Support"
    const val MyClient_MyLead = "MyClient_MyLead"
    const val MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY = "MyClients_MySnapshot_Productivity"
    const val SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT =
      "ProductSalesDirect1UP_BusinessSnapshot"
    const val ACTIONABLE = "Actionable"
    const val TEAM_LEAD = "Team_Lead"
    const val TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT = "Team_target_achievemnet"
    const val SELF_TARGET_ACHIEVEMNET = "target_achievemnet"
    const val AUSFB_TARGET_ACHIEVEMNET = "Target_Achievemnet_AU_Support"
    const val MONTH_ON_MONTH = "MonthOnMonth"
    const val MY_PERFORMANCE_INCENTIVE = "My_Performance_Incentive"
    const val PERFORMANCE_INCENTIVE_ACHIEVEMNET = "Performance_Incentive_Achievemnet"
    const val OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES =
      "OaoSalesOneup_TeamPerformanceAndIncentives"
    const val DASHBOARD_MODEL = "dashboardModel"
  }
}