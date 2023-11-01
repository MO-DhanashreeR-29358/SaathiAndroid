package com.mosl.tabapp.dashboard.leads.report

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.lead.report.LeadsReportModel
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.leads.LeadUiEvent
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.databinding.FragTableLeadShowBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LeadsReportFragment : CommonFragment(), LeadsReportView, View.OnClickListener {
  lateinit var binding: FragTableLeadShowBinding
  lateinit var dashboardModel: DashboardModel
  lateinit var map: HashMap<String, String>
  lateinit var widgetSystemName: String
  private val addtionalParameterMap: Map<String, String> =
    HashMap()

  @Inject
  lateinit var presenter: LeadsReportPresenter

  private val cellEvent =
    PublishSubject.create<LeadUiEvent>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragTableLeadShowBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    hideAppTitle()
    setUpView()
  }

  override fun getLayout(): Int {
    return R.layout.frag_table_lead_show
  }

  private fun setUpView() {
    dashboardModel =
      arguments?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
    binding.tvShowLess.setOnClickListener(this)
  }

  override fun renderData(uiModel: UiModel<LeadsReportModel>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      Timber.d("LeadsReportFragment renderData showOnFailurePopup ${it.message}")

      //showOnFailurePopup(it)
      binding.table.visibility = View.GONE
      binding.tvNochart.visibility = View.VISIBLE
    }
    uiModel.onSuccess {
      if (it.header.size > 0) {
        it.render()
      } else {
        binding.table.visibility = View.GONE
        binding.tvNochart.visibility = View.VISIBLE
      }

    }
  }

  private fun LeadsReportModel.render() {
    val leadTableAdapter =
      LeadsTableAdapter(
        requireActivity(),
        cellEvent
      )
    binding.table.adapter = leadTableAdapter

    leadTableAdapter.setHeaders(header)
    leadTableAdapter.setHeadersSecond(secondHeader)
    leadTableAdapter.setTableMap(groupData)

    listData.forEach {
      leadTableAdapter.renderData(it)
    }

  }

  override fun onClick(view: View) {
    val id = view.id
    val args = Bundle()
    val leadsFragment: CommonFragment = LeadsFragment()
    if (id == R.id.tvShowLess) {
      arguments?.apply {
        args.putInt(
          LeadsFragment.FLT_BANKNAME,
          this.getInt(LeadsFragment.FLT_BANKNAME)
        )
        args.putInt(
          LeadsFragment.FLT_BANKBRANCHCODE,
          this.getInt(LeadsFragment.FLT_BANKBRANCHCODE)
        )
        args.putInt(
          LeadsFragment.FLT_BANKSUBBROKERCODE,
          this.getInt(LeadsFragment.FLT_BANKSUBBROKERCODE)
        )
        args.putSerializable(MOSLConstants.DASHBOARD_MODEL, dashboardModel)
        args.putInt(
          LeadsFragment.PERIOD_FILTER,
          this.getInt(LeadsFragment.PERIOD_FILTER)
        )
        args.putInt(
          LeadsFragment.PRODUCT_FILTER,
          this.getInt(LeadsFragment.PRODUCT_FILTER)
        )
        args.putInt(
          LeadsFragment.SOURCE_FILTER,
          this.getInt(LeadsFragment.SOURCE_FILTER)
        )
      }
      leadsFragment.arguments = args
      requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()
    }
  }

  override fun getWidgetSystemsName(): String? {
    return arguments?.getString(MOSLConstants.SYSTEM_NAME)
  }

  override fun getAddtionalParameterMap(): Map<String, String> {
    return arguments?.getSerializable(MOSLConstants.MAP) as HashMap<String, String>
  }

  override fun cellEvent(): Observable<LeadUiEvent.CellClick> {
    return cellEvent.ofType()
  }

}