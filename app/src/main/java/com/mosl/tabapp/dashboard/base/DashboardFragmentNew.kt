package com.mosl.tabapp.dashboard.base

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.clients.ClientsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.databinding.FragDashboardBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.initialEmail
import com.mosl.tabapp.util.initiateCall
import io.reactivex.Observable
import kotlinx.android.synthetic.main.frag_dashboard.ivActionableInsights
import kotlinx.android.synthetic.main.frag_dashboard.ivTargetAchievementADV
import javax.inject.Inject

class DashboardFragmentNew : CommonFragment(), DashboardView, View.OnClickListener {
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
    binding.rlClientLeads.setOnClickListener(this)
    binding.rlTargetAchievementADV.setOnClickListener(this)
    binding.rlMonthOnMonth.setOnClickListener(this)
    binding.rlBusinessSnapshotHead.setOnClickListener(this)
    binding.rlTeamTargetAchievement.setOnClickListener(this)
    binding.rlPerformanceAndIncentivesHeader.setOnClickListener(this)
    if (userManager.getUser()!!.isAufsb()) {
//      binding.layoutAufsb.lldashboardAufsb.visibility = View.VISIBLE
      binding.ivActionableInsights.visibility = View.GONE
      binding.tvMain.visibility = View.GONE
    } else {
//      binding.layoutAufsb.lldashboardAufsb.visibility = View.GONE
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
        if (dashboardList.size > 0) {
          currentDashboard = dashboardList.get(0)
          renderDashboard(dashboardData)
        }
      }
    }
  }

  private fun renderDashboard(dashboardData: DashboardData) {
    for (group in dashboardData.groups) {
      lateinit var linearLayout: LinearLayout
      lateinit var icon: ImageView
      val systemName = group.systemName
      if (systemName.equals(ACTIONABLE)) {
        ivActionableInsights.visibility = View.VISIBLE
      } else if (systemName.equals(MyClient_MyLead)) {
        binding.llClient.visibility = View.VISIBLE
        setClientLayoutVisibililty(systemName)
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        loadClientsFragment()
        DashboardSystemModel.setLeadSystemName(systemName)
        DashboardSystemModel.setClientSystemName(systemName)
      } else if (systemName.equals(MY_LEAD)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadsFragment()
        DashboardSystemModel.setLeadSystemName(systemName)
      } else if (systemName.equals(MY_LEAD_AU_SUPPORT)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadsFragment()
        DashboardSystemModel.setLeadSystemName(systemName)
      } else if (systemName.equals(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadClientsFragment()
        DashboardSystemModel.setBusSnapshotAdvisorSystemName(systemName)
        DashboardSystemModel.setClientSystemName(systemName)
        DashboardSystemModel.setProductivitySystemName(systemName)
      } else if (systemName.equals(SYS_LEAD_SNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadBusinessSnapshotFragment()
        DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
        DashboardSystemModel.setLeadSystemName(systemName)
      } else if (systemName.equals(TEAM_LEAD)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadsFragment()
        DashboardSystemModel.setLeadSystemName(systemName)
      } else if (systemName.equals(SYS_MY_CLIENTS_SNAPSHOOT)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadClientsFragment()
        DashboardSystemModel.setClientSystemName(systemName)
        DashboardSystemModel.setClientTradedSystemName(systemName)
      } else if (systemName.equals(SYS_TEAM_SNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadBusinessSnapshotFragment()
        DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
      } else if (systemName.equals(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT)) {
        binding.llClient.visibility = View.VISIBLE
        linearLayout = binding.llClientsLeads
        icon = binding.ivClientLeads
        setClientLayoutVisibililty(systemName)
        loadLeadBusinessSnapshotFragment()
        DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
        DashboardSystemModel.setLeadSystemName(systemName)
      } else if (systemName.equals(SYS_BUSINESS_SNAPSHOT)) {
        binding.llSnapshot.visibility = View.VISIBLE
        linearLayout = binding.llBusinessSnapshotHead
        icon = binding.ivBusinessSnapshotHead
        loadLeadBusinessSnapshotFragment()
        DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
      } else if (systemName.equals(SYS_RH_BUSINESS_SNAPSHOT)) {
        binding.llSnapshot.visibility = View.VISIBLE
        linearLayout = binding.llBusinessSnapshotHead
        icon = binding.ivBusinessSnapshotHead
        loadLeadBusinessSnapshotFragment()
        DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
      } else if (systemName.equals(SELF_TARGET_ACHIEVEMNET)) {
        binding.txtTarget.setText(currentDashboard.getGroupModel(systemName).displayName)
        binding.llTarget.visibility = View.VISIBLE
        linearLayout = binding.llTargetAchievementADV
        icon = binding.ivTargetAchievementADV
        loadTargetAchievementFragment()
        DashboardSystemModel.setTargetAchivementSystemName(systemName)
      } else if (systemName.equals(AUSFB_TARGET_ACHIEVEMNET)) {
        binding.txtTarget.setText(currentDashboard.getGroupModel(systemName).getDisplayName());
        binding.llTarget.visibility = View.VISIBLE
        linearLayout = binding.llTargetAchievementADV
        icon = ivTargetAchievementADV
        loadTargetAchievementFragment()
        DashboardSystemModel.setTargetAchivementSystemName(systemName)
      } else if (systemName.equals(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT)) {
        binding.txtTarget.setText(currentDashboard.getGroupModel(systemName).getDisplayName());
        binding.llTarget.visibility = View.VISIBLE
        linearLayout = binding.llTeamTargetAchievement
        icon = binding.ivTeamTargetAchievement
        loadTargetAchievementFragment()
        DashboardSystemModel.setTargetAchivementSystemName(systemName)
      } else if (systemName.equals(MONTH_ON_MONTH)) {
        binding.llMonthTrend.visibility = View.VISIBLE
        linearLayout = binding.llMonthvsTrends
        icon = binding.ivMonthOnMonth
        loadMonthTrendsFragment()
        DashboardSystemModel.setMonthTrends(systemName)
      } else if (systemName.equals(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
        binding.tvPerformanceAndIncentivesHeader.setText(
            currentDashboard.getGroupModel(
                PERFORMANCE_INCENTIVE_ACHIEVEMNET
            ).displayName
        )
        binding.llPerformanceAndIncentives.visibility = View.VISIBLE
        linearLayout = binding.llPerformanceAndIncentivesHeader
        icon = binding.ivPerformanceAndIncentivesHeader
      } else if (systemName.equals(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)
          || systemName.equals(MY_PERFORMANCE_INCENTIVE)
      ) {
        binding.llPerformanceAndIncentives.visibility = View.VISIBLE
      }
      if (!systemName.equals(ACTIONABLE)) {
        setIcon(linearLayout, icon)
        if (linearLayout.visibility != View.VISIBLE) {
          toggleSection(linearLayout, icon)
        }
      }
    }
  }

  fun setClientLayoutVisibililty(systemName: String) {
    binding.txtClients.setText(currentDashboard.getGroupModel(systemName).displayName)
    when (systemName) {
      MyClient_MyLead -> {
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
      }
      MY_LEAD -> {
        binding.btnClient.visibility = View.GONE
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
      }
      MY_LEAD_AU_SUPPORT -> {
        binding.btnClient.visibility = View.GONE
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
      }
      SYS_LEAD_SNAPSHOT -> {
        binding.btnClient.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
      }
      TEAM_LEAD -> {
        binding.btnClientTraded.visibility = View.GONE
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
        binding.btnClient.visibility = View.GONE
      }
      SYS_MY_CLIENTS_SNAPSHOOT -> {
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnLeads.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
      }
      SYS_TEAM_SNAPSHOT -> {
        binding.btnClient.visibility = View.GONE
        binding.btnLeads.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
      }
      SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT -> {
        binding.btnClient.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
        binding.btnProductivity.visibility = View.GONE
        binding.btnBusinessSnapshotAdvisor.visibility = View.GONE
      }
    }
  }

  fun toggleSection(
    parent: View,
    icon: ImageView
  ) {
    if (parent.visibility == View.VISIBLE) {
      parent.visibility = View.GONE
      icon.setImageResource(R.drawable.plus)
    } else {
      parent.visibility = View.VISIBLE
      icon.setImageResource(R.drawable.minus)
    }
  }

  fun setIcon(
    parent: View,
    icon: ImageView
  ) {
    if (parent.visibility == View.VISIBLE) {
      icon.setImageResource(R.drawable.minus)
    } else {
      icon.setImageResource(R.drawable.plus)
    }
  }

  fun setIncentiveVisibility(systemName: String) {
    if (currentDashboard != null) {
      val groupModel = currentDashboard.getGroupModel(systemName)
      val widgetList = groupModel.widgetList
      for (i in widgetList.indices) {
        val widgetTypes = widgetList[i]
            .widgetTypeList[0]
            .widgetTypes
        if (widgetTypes.equals(MOSLConstants.PERFORMANCE_INCENTIVE, ignoreCase = true)) {
//          incentiveWebServiceCall(widgetList[i].systemName)
        } else if (widgetTypes.equals(MOSLConstants.CURRENT_QUARTER_STATUS, ignoreCase = true)) {
//          currentQuarterStatusWebServiceCall(widgetList[i].systemName)
          binding.llCurrentQuarterStatusAdviosory.setVisibility(View.VISIBLE)
        }
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
    val targetFragment = TargetFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    targetFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llTargetAchievementADV, targetFragment)
        .commit()
  }

  override fun loadLeadBusinessSnapshotFragment() {
    val leadsFragment = BusinessSnapshotFragment()
    setTabActive(binding.btnBusinessSnapshot)
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()
  }

  override fun loadProductivityFragment() {
    setTabActive(binding.btnProductivity)
    val productivityFragment = ProductivityFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    productivityFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, productivityFragment)
        .commit()
  }

  override fun loadLeadsFragment() {
    val leadsFragment = LeadsFragment()
    setTabActive(binding.btnLeads)
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()

  }

  val args = Bundle()
  override fun loadClientsFragment() {
    val clientsFragment = ClientsFragment()
    setTabActive(binding.btnClient)
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    clientsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, clientsFragment)
        .commit()
  }

  override fun loadClientsTradedFragment() {
    val clientsFragment = ClientsTradedYesterdayFragment()
    setTabActive(binding.btnClient)
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    clientsFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, clientsFragment)
        .commit()
  }

  override fun loadBusinessSnapshotAdvisorFragment() {
    val businessSnapshotAdvFragment = BusinessSnapshotAdvFragment()
    setTabActive(binding.btnBusinessSnapshotAdvisor)
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    businessSnapshotAdvFragment.arguments = args
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, businessSnapshotAdvFragment)
        .commit()
  }

  fun setTabActive(button: Button) {
    button.setBackground(resources.getDrawable(R.drawable.orange))
    button.setTextColor(resources.getColor(R.color.white))
  }

  override fun onClientClicked(): Observable<Unit> {
    return binding.btnClient.clicks()
  }

  override fun onBusinessSnapshotClicked(): Observable<Unit> {
    return binding.btnBusinessSnapshot.clicks()
  }

  override fun onLeadClicked(): Observable<Unit> {
    return binding.btnLeads.clicks()
  }

  override fun onProductivityClicked(): Observable<Unit> {
    return binding.btnProductivity.clicks()
  }

  override fun onBusinessSnapshotAdvisorClicked(): Observable<Unit> {
    return binding.btnBusinessSnapshotAdvisor.clicks()
  }

  override fun onClientTradedClicked(): Observable<Unit> {
    return binding.btnClientTraded.clicks()
  }

  override fun onLeadCreationClicked(): Observable<Unit> {
    return binding.leadcreation.clicks()
  }

//  override fun onFreshRegistrationClicked(): Observable<Unit>{return binding.layoutAufsb.rlFreshRegistration.clicks()}
//  override fun onExistingRegistrationClicked(): Observable<Unit>{return binding.layoutAufsb.rlExisting.clicks()}

  override fun collapseActionsMenu() {
    binding.floatingActionsMenu.collapse()
  }

//  override fun onTelephone1Clicked(): Observable<Unit>{ return binding.layoutAufsb.txtTelephoneNo1.clicks()}
//  override fun onTelephone2Clicked(): Observable<Unit>{ return binding.layoutAufsb.txtTelephoneNo2.clicks()}
//  override fun onEmailClicked(): Observable<Unit>{ return binding.layoutAufsb.llEmail.clicks()}

  override fun showCallPopup() {
    MSFDialog.alertDialog(
        requireActivity(), 0,
        resources.getString(string.motilal_oswal), "Do you want to call?", "Yes",
        "No", true
    ) { action: Action, data: Array<Any?>? ->
      if (action == OK) {
        requireContext().initiateCall("02239982515")
      }
    }
  }

  override fun showEmailDialog() {
    requireContext().initialEmail("rggupta72@gmail.com")
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

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.rlClientLeads -> toggleSection(binding.llClientsLeads, binding.ivClientLeads)
      R.id.rlTargetAchievementADV -> toggleSection(
          binding.llTargetAchievementADV, binding.ivTargetAchievementADV
      )
      R.id.rlMonthOnMonth -> toggleSection(binding.llMonthvsTrends, binding.ivMonthOnMonth)
      R.id.rlBusinessSnapshotHead -> toggleSection(
          binding.llBusinessSnapshotHead, binding.ivBusinessSnapshotHead
      )
      R.id.rlTeamTargetAchievement -> toggleSection(
          binding.llTeamTargetAchievement, binding.ivTeamTargetAchievement
      )
      R.id.rlPerformanceAndIncentivesHeader -> toggleSection(
          binding.llPerformanceAndIncentivesHeader, binding.ivPerformanceAndIncentivesHeader
      )
    }
  }
}