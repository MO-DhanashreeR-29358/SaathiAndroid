package com.mosl.tabapp.dashboard.tradeyesterday

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayResponseModel
import com.mosl.shared.util.toDecimalsOrDash
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragClientTradedBinding
import com.mosl.tabapp.databinding.FragClientTradedBottomBinding
import com.mosl.tabapp.databinding.FragClientTradedTopBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class ClientsTradedYesterdayFragment : CommonFragment(), ClientsTradedYesterdayView {
  private lateinit var binding: FragClientTradedBinding
  @Inject
  lateinit var presenter: ClientsTradedYesterdayPresenter
  @Inject
  lateinit var dashboardModel: DashboardModel
  private lateinit var model: WidgetModel
  private lateinit var widgetSystemName: String
  private val dataEvent = PublishSubject.create<Boolean>()
  private val arrayListClient = mutableListOf<HashMap<String, String>>()
  private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()

  override fun getLayout(): Int {
    return R.layout.frag_client_traded
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragClientTradedBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    val groupModel =
      dashboardModel.getGroupModel(DashboardSystemModel.getClientTradedSystemName())
    val widgetList = groupModel.widgetList
    if (widgetList.size > 0) {
      model = widgetList[1]
      widgetSystemName = model.systemName
    }
  }

  override fun getGroupName(): String? {
    return model.name
  }

  override fun getWidgetSystemsName(): String? {
    return model.systemName
  }

  override fun getClientList(): MutableList<HashMap<String, String>> {
    return arrayListClient
  }

  override fun getAdditionalParameterMap(): MutableMap<String, String> {
    val additionalParameterMap: MutableMap<String, String> = HashMap()
    additionalParameterMap[MOSLConstants.SELF_KEY] = dashboardModel.dashboardFor
    return additionalParameterMap
  }

  override fun renderData(uiModel: UiModel<ClientTradeYesterdayResponseModel>) {
    toggleProgress(uiModel.inProgress)
    arrayListClient.clear()
    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      clientHideProgress()
      binding.tvNochart.visibility = View.VISIBLE
      dataEvent.onNext(true)
    }

    uiModel.onSuccess {
      clientHideProgress()
      it.table.apply {
        for (i in this.indices) {
          val fieldData = HashMap<String, String>()
          fieldData[REVENUE_TYPE] = stringOrEmpty(this[i].revenueType)
          fieldData[NO_OF_CLIENTS] = stringOrEmpty(this[i].noOfClients?.toString())
          fieldData[REVENUE_GENERATED] = stringOrEmpty(this[i].revenueGenerated?.toDecimalsOrDash())
          arrayListClient.add(fieldData)
          dataEvent.onNext(true)
        }
      }
    }
  }

  override fun renderUI() {
    binding.tableLayout.apply {
      removeAllViews()
      val inflater = requireActivity().layoutInflater
      val topBinding =
        FragClientTradedTopBinding.inflate(inflater)
      val headerView = topBinding.root
      addView(headerView)
      if (arrayListClient.size > 0) {
        binding.tvNochart.visibility = View.GONE
        for (i in arrayListClient.indices) {
          val bottomBinding =
            FragClientTradedBottomBinding.inflate(inflater)
          val tableRow = bottomBinding.root
          bottomBinding.apply {
            tvSegment.text = arrayListClient[i][REVENUE_TYPE]
            tvNoOfClients.text = arrayListClient[i][NO_OF_CLIENTS]
            tvRevenueGenerated.text = arrayListClient[i][REVENUE_GENERATED]

            val additionalParameters: MutableMap<String, String> = HashMap()
            tvNoOfClients.setOnClickListener {
              additionalParameters.apply {
                putAll(getAdditionalParameterMap())
                this[MOSLConstants.ROW_FILTER] = tvSegment.text.toString()
              }
              if (tvNoOfClients.text.toString() != "0") {
                navigateEvent.onNext(additionalParameters)
              }
            }

            tvRevenueGenerated.setOnClickListener {
              additionalParameters.apply {
                clear()
                putAll(getAdditionalParameterMap())
                additionalParameters[MOSLConstants.ROW_FILTER] = tvSegment.text.toString()
              }
              if (tvRevenueGenerated.text.toString() != "0") {
                navigateEvent.onNext(additionalParameters)
              }
            }
          }
          binding.tableLayout.addView(tableRow)
        }
      } else {
        binding.tvNochart.visibility = View.VISIBLE
      }
    }
  }

  override fun onDataEvent(): Observable<Boolean> {
    return dataEvent.ofType()
  }

  override fun onNavigateEvent(): Observable<MutableMap<String, String>> {
    return navigateEvent.ofType()
  }

  private fun clientHideProgress() {
    binding.pBar.visibility = View.GONE
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.stop()
  }
}