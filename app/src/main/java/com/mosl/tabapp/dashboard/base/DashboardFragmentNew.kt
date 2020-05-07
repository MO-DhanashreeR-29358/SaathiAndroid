package com.mosl.tabapp.dashboard.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.getbase.floatingactionbutton.FloatingActionButton
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.Incentive
import com.mosl.mobile.domain.dashboard.IncentivePolicy
import com.mosl.mobile.domain.dashboard.IncentiveTableData
import com.mosl.mobile.domain.dashboard.IncentiveTableData1
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.R.string
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.MONTH_ON_MONTH_ACCOUNT_REVENUE
import com.mosl.tabapp.constants.MOSLConstants.PERFORMANCE_INCENTIVE
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightDialogFragment
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.ActionableCount
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.CurrentQuarterStatus
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.PerformanceIncentive
import com.mosl.tabapp.dashboard.clients.ClientsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetFragment
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.databinding.FragDashboardBinding
import com.mosl.tabapp.databinding.ItemTableRowBinding
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.downloadImage
import com.mosl.tabapp.util.initialEmail
import com.mosl.tabapp.util.initiateCall
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject

class DashboardFragmentNew : CommonFragment(), DashboardView, View.OnClickListener {
  internal lateinit var binding: FragDashboardBinding

  @Inject
  lateinit var presenter: DashboardPresenter
  val subject: PublishSubject<DashboardUiEvent> = PublishSubject.create()

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
      binding.layoutAufsb.lldashboardAufsb.visibility = View.VISIBLE
      binding.ivActionableInsights.visibility = View.GONE
      binding.tvMain.visibility = View.GONE
    } else {
      binding.layoutAufsb.lldashboardAufsb.visibility = View.GONE
    }
    val periodAdapter = SpinnerAdapterFactory.getDefaultAdapter<String>(requireContext())
    binding.layoutIncentives.spQuarter.adapter = periodAdapter
    val eventTypeArray =
      resources.getStringArray(R.array.SelectQuarter)
    val arrEventType = listOf(*eventTypeArray)
    periodAdapter.setSpinnerData(binding.layoutIncentives.spQuarter, arrEventType)
  }

  override fun render(uiModel: UiModel<List<DashboardData>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      dashboardList.clear()
      for (dashboardData in it) {
        fillAvailableWidgetModel(dashboardData)
      }
      if (dashboardList.size > 0) {
        currentDashboard = dashboardList[0]
        createFloatingActionButtons()
        renderDashboard(currentDashboard)
      }
    }
  }

  private fun createFloatingActionButtons() {
    if (dashboardList.size > 1)
      for (dashboardModel in dashboardList) {
        val button = FloatingActionButton(context)
        button.title = dashboardModel.name
        button.size = FloatingActionButton.SIZE_MINI
        button.colorNormal = ContextCompat.getColor(requireContext(), color.white)
        ContextCompat.getDrawable(requireContext(), drawable.ic_create_lead)?.let {
          button.setIconDrawable(it)
        }
        binding.floatingActionsMenu.addButton(button)
        button.setOnClickListener { v: View? ->
          hideKeyboard(v)
          binding.tvMain.requestFocus()
          binding.floatingActionsMenu.collapse()
          currentDashboard = dashboardModel
          renderDashboard(currentDashboard)
        }
      }

  }

  private fun renderDashboard(currentDashboard: DashboardModel) {
    binding.apply {
      llClient.visibility = View.GONE
      ivActionableInsights.visibility = View.GONE
      llTarget.visibility = View.GONE
      llTeamTarget.visibility = View.GONE
      llPerformanceAndIncentives.visibility = View.GONE
      llCurrentQuarterStatusAdviosory.visibility = View.GONE
      llCurrentQuarterStatusOneUp.visibility = View.GONE
      llMonthTrend.visibility = View.GONE
      llSnapshot.visibility = View.GONE
    }
    for (group in currentDashboard.groupList) {
      lateinit var linearLayout: LinearLayout
      lateinit var icon: ImageView
      val systemName = group.systemName
      when (systemName) {
        ACTIONABLE -> {
          binding.ivActionableInsights.visibility = View.VISIBLE
          updateCount(systemName)
        }
        MyClient_MyLead -> {
          binding.llClient.visibility = View.VISIBLE
          setClientLayoutVisibililty(systemName)
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          loadClientsFragment()
          DashboardSystemModel.setLeadSystemName(systemName)
          DashboardSystemModel.setClientSystemName(systemName)
        }
        MY_LEAD -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadsFragment()
          DashboardSystemModel.setLeadSystemName(systemName)
        }
        MY_LEAD_AU_SUPPORT -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadsFragment()
          DashboardSystemModel.setLeadSystemName(systemName)
        }
        MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadClientsFragment()
          DashboardSystemModel.setBusSnapshotAdvisorSystemName(systemName)
          DashboardSystemModel.setClientSystemName(systemName)
          DashboardSystemModel.setProductivitySystemName(systemName)
        }
        SYS_LEAD_SNAPSHOT -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadBusinessSnapshotFragment()
          DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
          DashboardSystemModel.setLeadSystemName(systemName)
        }
        TEAM_LEAD -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadsFragment()
          DashboardSystemModel.setLeadSystemName(systemName)
        }
        SYS_MY_CLIENTS_SNAPSHOOT -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadClientsFragment()
          DashboardSystemModel.setClientSystemName(systemName)
          DashboardSystemModel.setClientTradedSystemName(systemName)
        }
        SYS_TEAM_SNAPSHOT -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadBusinessSnapshotFragment()
          DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
        }
        SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT -> {
          binding.llClient.visibility = View.VISIBLE
          linearLayout = binding.llClientsLeads
          icon = binding.ivClientLeads
          setClientLayoutVisibililty(systemName)
          loadLeadBusinessSnapshotFragment()
          DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
          DashboardSystemModel.setLeadSystemName(systemName)
        }
        SYS_BUSINESS_SNAPSHOT -> {
          binding.tvSnapshot.text = BUSINESS_SNAPSHOT_TITLE
          binding.llSnapshot.visibility = View.VISIBLE
          linearLayout = binding.llBusinessSnapshotHead
          icon = binding.ivBusinessSnapshotHead
          loadBusinessSnapShot()
          DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
        }
        SYS_RH_BUSINESS_SNAPSHOT -> {
          binding.tvSnapshot.text = BUSINESS_SNAPSHOT_TITLE
          binding.llSnapshot.visibility = View.VISIBLE
          linearLayout = binding.llBusinessSnapshotHead
          icon = binding.ivBusinessSnapshotHead
          loadBusinessSnapShot()
          DashboardSystemModel.setBusinessSnapshotSystemName(systemName)
        }
        SELF_TARGET_ACHIEVEMNET -> {
          binding.txtTarget.text = currentDashboard.getGroupModel(systemName).displayName
          binding.llTarget.visibility = View.VISIBLE
          linearLayout = binding.llTargetAchievementADV
          icon = binding.ivTargetAchievementADV
          loadTargetAchievementFragment()
          DashboardSystemModel.setTargetAchivementSystemName(systemName)
        }
        AUSFB_TARGET_ACHIEVEMNET -> {
          binding.txtTarget.text = currentDashboard.getGroupModel(systemName).displayName
          binding.llTarget.visibility = View.VISIBLE
          linearLayout = binding.llTargetAchievementADV
          icon = binding.ivTargetAchievementADV
          loadTargetAchievementFragment()
          DashboardSystemModel.setTargetAchivementSystemName(systemName)
        }
        TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT -> {
          binding.txtTeamTarget.text = currentDashboard.getGroupModel(systemName).displayName
          binding.llTeamTarget.visibility = View.VISIBLE
          linearLayout = binding.llTeamTargetAchievement
          icon = binding.ivTeamTargetAchievement
          loadTeamTargetAchievementFragment()
          DashboardSystemModel.setTargetAchivementSystemName(systemName)
        }
        MONTH_ON_MONTH -> {
          binding.txtMonthTrends.text = TRENDS_TITLE
          binding.llMonthTrend.visibility = View.VISIBLE
          linearLayout = binding.llMonthvsTrends
          icon = binding.ivMonthOnMonth
          loadMonthTrendsFragment()
          DashboardSystemModel.setMonthTrends(systemName)
        }
        MONTH_ON_MONTH_ACCOUNT_REVENUE -> {
          binding.txtMonthTrends.text = TRENDS_TITLE
          binding.llMonthTrend.visibility = View.VISIBLE
          linearLayout = binding.llMonthvsTrends
          icon = binding.ivMonthOnMonth
          loadMonthTrendsFragment()
          DashboardSystemModel.setMonthTrends(systemName)
        }
        PERFORMANCE_INCENTIVE_ACHIEVEMNET -> {
          setIncentiveVisibility(systemName)
          binding.tvPerformanceAndIncentivesHeader.text =
            currentDashboard.getGroupModel(
              PERFORMANCE_INCENTIVE_ACHIEVEMNET
            ).displayName

          binding.llPerformanceAndIncentives.visibility = View.VISIBLE
          linearLayout = binding.llPerformanceAndIncentivesHeader
          icon = binding.ivPerformanceAndIncentivesHeader
        }
        PERFORMANCE_INCENTIVE_ACHIEVEMNET_ONE -> {
          setIncentiveVisibility(systemName)
          binding.tvPerformanceAndIncentivesHeader.text =
            currentDashboard.getGroupModel(
              PERFORMANCE_INCENTIVE_ACHIEVEMNET_ONE
            ).displayName

          binding.llPerformanceAndIncentives.visibility = View.VISIBLE
          linearLayout = binding.llPerformanceAndIncentivesHeader
          icon = binding.ivPerformanceAndIncentivesHeader
        }
        OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES -> {
          setIncentiveVisibility(systemName)
          binding.tvPerformanceAndIncentivesHeader.text =
            currentDashboard.getGroupModel(
              OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES
            ).displayName

          binding.llPerformanceAndIncentives.visibility = View.VISIBLE
          linearLayout = binding.llPerformanceAndIncentivesHeader
          icon = binding.ivPerformanceAndIncentivesHeader
        }
        MY_PERFORMANCE_INCENTIVE -> {
          setIncentiveVisibility(systemName)
          binding.tvPerformanceAndIncentivesHeader.text =
            currentDashboard.getGroupModel(
              MY_PERFORMANCE_INCENTIVE
            ).displayName

          binding.llPerformanceAndIncentives.visibility = View.VISIBLE
          linearLayout = binding.llPerformanceAndIncentivesHeader
          icon = binding.ivPerformanceAndIncentivesHeader
        }
      }
      if (systemName != ACTIONABLE) {
        setIcon(linearLayout, icon)
        if (linearLayout.visibility != View.VISIBLE) {
          toggleSection(linearLayout, icon)
        }
      }
    }
  }

  private fun updateCount(systemName: String) {
    val actionableWidgetList =
      currentDashboard.getGroupModel(systemName).widgetList
    if (actionableWidgetList.isNotEmpty()) {
      for (model in actionableWidgetList) {
        subject.onNext(ActionableCount(model.systemName, getAddtionalParameterMap()))
      }
    }
  }

  private fun setClientLayoutVisibililty(systemName: String) {
    binding.txtClients.text = currentDashboard.getGroupModel(systemName).displayName
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
      MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY -> {
        binding.btnBusinessSnapshot.visibility = View.GONE
        binding.btnLeads.visibility = View.GONE
        binding.btnClientTraded.visibility = View.GONE
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

  override fun Incentive(): Observable<PerformanceIncentive> {
    return subject.ofType()
  }

  override fun CurrentQuarterStatus(): Observable<CurrentQuarterStatus> {
    return subject.ofType()
  }

  override fun ActionableCount(): Observable<ActionableCount> {
    return subject.ofType()
  }

  private fun toggleSection(
    parent: View,
    icon: ImageView
  ) {
    if (parent.visibility == View.VISIBLE) {
      parent.visibility = View.GONE
      icon.setImageResource(drawable.plus)
    } else {
      parent.visibility = View.VISIBLE
      icon.setImageResource(drawable.minus)
    }
  }

  private fun getAddtionalParameterMap(): Map<String, String> {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()
    try {
      addtionalParameterMap[SELF_KEY] = currentDashboard.dashboardFor
    } catch (ex: Exception) {
      ex.printStackTrace()
    }
    return addtionalParameterMap
  }

  fun setIcon(
    parent: View,
    icon: ImageView
  ) {
    if (parent.visibility == View.VISIBLE) {
      icon.setImageResource(drawable.minus)
    } else {
      icon.setImageResource(drawable.plus)
    }
  }

  override fun renderIncentivePolicy(uiModel: UiModel<IncentivePolicy>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      if (it.filePath.isEmpty()) {
        alertDialog(
          requireContext(), 0, resources.getString(string.motilal_oswal),
          MISSING_FILE, "OK", true, null
        )
      } else {
        requireContext().downloadImage(it.filePath)
        Toast.makeText(requireContext(), "Downloading ${it.filePath}...", Toast.LENGTH_SHORT)
          .show()
      }
    }
  }

  override fun renderActionableCount(uiModel: UiModel<Pair<com.mosl.mobile.domain.dashboard.ActionableCount, String>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onSuccess {
      val typeList = ArrayList<TypeActionableFilterModel>()

      val tables = it.first.table
      val tables1 = it.first.table1
      if (tables.isNotEmpty()) {
        for (table in tables) {
          val type = TypeActionableFilterModel(table.type)
          typeList.add(type)
        }
      }
      var count = ""
      if (tables1.isNotEmpty()) {
        count = tables1.get(0)
          .count.toString()
      }
      currentDashboard.setActionableCount(it.second, count, typeList)
    }
  }

  override fun renderIncentiveData(uiModel: UiModel<Incentive>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      incentiveData(it.tableData)
      if (it.tableData1.isNotEmpty())
        renderPerformance(it.tableData1)
    }
  }

  private fun renderPerformance(tableData: List<IncentiveTableData1>) {
    binding.performance.visibility = View.VISIBLE
    binding.performance.loadChart(tableData)
  }

  private fun incentiveData(tableData: List<IncentiveTableData>) {
    arrayListIncentive.clear()
    for (i in tableData.indices) {
      val data = tableData[i]
      val fieldData = HashMap<String, String>()
      fieldData[PARAMETERS] = data.parameters
      fieldData[Q1] = data.q1.toString()
      fieldData[Q2] = data.q2.toString()
      fieldData[Q3] = data.q3.toString()
      fieldData[Q4] = data.q4.toString()
      fieldData[YTD] = data.ytd.toString()
      arrayListIncentive.add(fieldData)
    }
    loadIncentiveView("Q1")
  }

  override fun loadIncentiveView(value: String) {
    binding.layoutIncentives.tableMData.removeAllViews()
    val inflater = LayoutInflater.from(requireContext())
    val headerBinding = ItemTableRowBinding.inflate(inflater)
    headerBinding.tvParameters.text = resources.getString(string.txt_label_parameters)
    headerBinding.tvMTDCOUNT.text = value
    headerBinding.tvYTDCOUNT.text = MOSLConstants.YTD
    binding.layoutIncentives.tableMData.addView(headerBinding.root)
    //adding head end
    if (arrayListIncentive.size > 0) {
      for (i in arrayListIncentive.indices) {
        val filterData: String = arrayListIncentive[i][value] ?: ""
        val parameter: String = arrayListIncentive[i][PARAMETERS] ?: ""
        val ytd: String = arrayListIncentive[i][YTD] ?: ""
        val childBinding = ItemTableRowBinding.inflate(inflater)
        childBinding.tvParameters.text = parameter
        childBinding.tvMTDCOUNT.text = filterData
        childBinding.tvYTDCOUNT.text = ytd
        childBinding.linearLayout.setBackgroundResource(if (i % 2 == 0) color.white else color.white)
        binding.layoutIncentives.tableMData.addView(childBinding.root)
      }
    }
    binding.layoutIncentives.llIncentive.visibility = View.VISIBLE
    binding.layoutIncentives.tvIncentives.text = resources.getString(string.txt_label_incentive)
  }

  private fun setIncentiveVisibility(systemName: String) {
    val groupModel = currentDashboard.getGroupModel(systemName)
    val widgetList = groupModel.widgetList
    for (i in widgetList.indices) {
      val widgetTypes = widgetList[i]
        .widgetTypeList[0]
        .widgetTypes
      if (widgetTypes.equals(PERFORMANCE_INCENTIVE, ignoreCase = true)) {
        subject.onNext(PerformanceIncentive(widgetList[i].systemName, getAddtionalParameterMap()))
      } else if (widgetTypes.equals(MOSLConstants.CURRENT_QUARTER_STATUS, ignoreCase = true)) {
        subject.onNext(CurrentQuarterStatus(widgetList[i].systemName, getAddtionalParameterMap()))
        binding.llCurrentQuarterStatusAdviosory.setVisibility(View.VISIBLE)
      }
    }

  }

  override fun renderStatus(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onSuccess {
      val response = JSONObject(uiModel.data)
      val status = response.get(STATUS)
        .toString()
      if (status == "true") {
        val jsonObject = response.getJSONObject(RESPONSE)
        renderCurrentQuarterStatus(jsonObject)
      } else {
        throw ServiceFailureException(response.get(MESSAGE).toString())
      }
    }
  }

  private fun renderCurrentQuarterStatus(jsonObject: JSONObject) {
    binding.currentQuarter.loadData(jsonObject)
  }

  private val dashboardList: ArrayList<DashboardModel> = ArrayList()
  private val arrayListIncentive: ArrayList<HashMap<String, String>> = ArrayList()
  private lateinit var currentDashboard: DashboardModel
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

  private fun loadTeamTargetAchievementFragment() {
    val args = Bundle()
    val leadsFragment = TeamTargetFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    leadsFragment.arguments = args
    requireActivity().supportFragmentManager
      .beginTransaction()
      .replace(R.id.llTeamTargetAchievement, leadsFragment)
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

  private fun loadBusinessSnapShot() {
    val fragment = BusinessSnapshotFragment()
    args.putSerializable(DASHBOARD_MODEL, currentDashboard)
    fragment.arguments = args
    requireActivity().supportFragmentManager
      .beginTransaction()
      .replace(R.id.llBusinessSnapshotHead, fragment)
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

  private fun setTabActive(button: Button) {
    binding.apply {
      btnBusinessSnapshot.background = ContextCompat.getDrawable(requireContext(), drawable.white)
      btnBusinessSnapshot.setTextColor(
        ContextCompat.getColor(
          requireContext(),
          color.Button_text_color
        )
      )
      btnBusinessSnapshotAdvisor.background =
        ContextCompat.getDrawable(requireContext(), drawable.white)
      btnBusinessSnapshotAdvisor.setTextColor(
        ContextCompat.getColor(
          requireContext(),
          color.Button_text_color
        )
      )
      btnClient.background = ContextCompat.getDrawable(requireContext(), drawable.white)
      btnClient.setTextColor(ContextCompat.getColor(requireContext(), color.Button_text_color))
      btnProductivity.background = ContextCompat.getDrawable(requireContext(), drawable.white)
      btnProductivity.setTextColor(
        ContextCompat.getColor(
          requireContext(),
          color.Button_text_color
        )
      )
      btnLeads.background = ContextCompat.getDrawable(requireContext(), drawable.white)
      btnLeads.setTextColor(ContextCompat.getColor(requireContext(), color.Button_text_color))
      btnClientTraded.background = ContextCompat.getDrawable(requireContext(), drawable.white)
      btnClientTraded.setTextColor(
        ContextCompat.getColor(
          requireContext(),
          color.Button_text_color
        )
      )
    }
    button.background = ContextCompat.getDrawable(requireContext(), drawable.orange)
    button.setTextColor(ContextCompat.getColor(requireContext(), color.white))
  }

  override fun onActionableInsightsClicked(): Observable<Unit> {
    return binding.ivActionableInsights.clicks()
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

  override fun onQuarterChanged(): Observable<Int> {
    return binding.layoutIncentives.spQuarter.itemSelections()
  }

  override fun getSelectedQuarter(): String {
    return binding.layoutIncentives.spQuarter.selectedItem.toString()
  }

  override fun onFreshRegistrationClicked(): Observable<Unit> {
    return binding.layoutAufsb.rlFreshRegistration.clicks()
  }

  override fun onExistingRegistrationClicked(): Observable<Unit> {
    return binding.layoutAufsb.rlExisting.clicks()
  }

  override fun collapseActionsMenu() {
    binding.floatingActionsMenu.collapse()
  }

  override fun onTelephone1Clicked(): Observable<String> {
    return binding.layoutAufsb.txtTelephoneNo1.clicks().map {
      binding.layoutAufsb.txtTelephoneNo1.text.toString()
    }
  }

  override fun onTelephone2Clicked(): Observable<String> {
    return binding.layoutAufsb.txtTelephoneNo2.clicks().map {
      binding.layoutAufsb.txtTelephoneNo2.text.toString()
    }
  }

  override fun onEmailClicked(): Observable<String> {
    return binding.layoutAufsb.llEmail.clicks().map {
      binding.layoutAufsb.llEmail.tv_email.text.toString()
    }
  }

  override fun onDetailIncentiveClicked(): Observable<Unit> {
    return binding.tvDetailIncentive.clicks()
  }

  override fun getDashboardID(): String {
    return currentDashboard.id
  }

  override fun showCallPopup(number: String) {
    alertDialog(
      requireActivity(), 0,
      resources.getString(string.motilal_oswal), "Do you want to call?", "Yes",
      "No", true
    ) { action: Action, _: Array<Any?>? ->
      if (action == OK) {
        requireContext().initiateCall(number)
      }
    }
  }

  override fun showEmailDialog(email: String) {
    requireContext().initialEmail(email)
  }

  override fun showInsightsDialog() {
    hideKeyboard(binding.ivActionableInsights)
    val dialogFragment = ActionableInsightDialogFragment()
    val widgetList = currentDashboard.getGroupModel(
      ACTIONABLE
    ).widgetList as ArrayList<WidgetModel>
    val bundle = Bundle()
    bundle.putSerializable(ActionableInsightDialogFragment.ARG_LIST_DATA, widgetList)
    bundle.putString(
      ActionableInsightDialogFragment.ARG_DASHBOARD_FOR,
      currentDashboard.dashboardFor
    )
    dialogFragment.arguments = bundle
    dialogFragment.show(requireFragmentManager(), "Actionable Dialog")
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
    const val PERFORMANCE_INCENTIVE_ACHIEVEMNET_ONE = "Performance_Incentive_Achievemnet1"
    const val OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES =
      "OaoSalesOneup_TeamPerformanceAndIncentives"
    const val DASHBOARD_MODEL = "dashboardModel"
    const val SELF_KEY = "flt_hierarchy"
    const val PARAMETERS = "Parameters"
    const val Q1 = "Q1"
    const val Q2 = "Q2"
    const val Q3 = "Q3"
    const val Q4 = "Q4"
    const val YTD = "YTD"
    const val BUSINESS_SNAPSHOT_TITLE = "Business Snapshot"
    const val TRENDS_TITLE = "Month on Month Trend"
    const val MISSING_FILE = "File Not Found"
    const val STATUS = "Status"
    const val MESSAGE = "Message"
    const val RESPONSE = "Response"
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