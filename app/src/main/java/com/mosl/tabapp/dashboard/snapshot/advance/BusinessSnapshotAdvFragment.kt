package com.mosl.tabapp.dashboard.snapshot.advance

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.snapshot.BusinessSnapshotResponseModel
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.COL_FILTER
import com.mosl.tabapp.constants.MOSLConstants.MTD
import com.mosl.tabapp.constants.MOSLConstants.MTD_CNT
import com.mosl.tabapp.constants.MOSLConstants.PARAMETER
import com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER
import com.mosl.tabapp.constants.MOSLConstants.SELF_KEY
import com.mosl.tabapp.constants.MOSLConstants.WIDGETTYPE
import com.mosl.tabapp.constants.MOSLConstants.YTD_CNT
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragBusinessSnapshotBottomBinding
import com.mosl.tabapp.databinding.FragBusinessSnapshotTopBinding
import com.mosl.tabapp.databinding.FragmentClientsNewBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import java.util.HashMap
import javax.inject.Inject

class BusinessSnapshotAdvFragment : CommonFragment(), BusinessSnapshotAdvView {
  private lateinit var binding: FragmentClientsNewBinding
  @Inject
  lateinit var presenter: BusinessSnapshotAdvPresenter
  @Inject
  lateinit var dashboardModel: DashboardModel
  private lateinit var model: WidgetModel
  private lateinit var widgetSystemName: String
  private val dataEvent = PublishSubject.create<Boolean>()
  private val arrayListClient = mutableListOf<HashMap<String, String>>()
  private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()

  override fun getLayout(): Int {
    return R.layout.fragment_clients_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentClientsNewBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    binding.spPeriod.visibility = View.GONE

    val groupModel =
      dashboardModel.getGroupModel(DashboardSystemModel.getBusSnapshotAdvisorSystemName())
    val widgetList = groupModel.widgetList
    if (widgetList.size > 0) {
      model = widgetList[1]
      widgetSystemName = model.systemName
    }
  }

  override fun getWidgetSystemsName(): String? {
    return model.systemName
  }

  override fun getClientList(): MutableList<HashMap<String, String>> {
    return arrayListClient
  }

  override fun getAdditionalParameterMap(): MutableMap<String, String> {
    val additionalParameterMap: MutableMap<String, String> = HashMap()
    additionalParameterMap[SELF_KEY] = dashboardModel.dashboardFor
    return additionalParameterMap
  }

  override fun renderData(uiModel: UiModel<BusinessSnapshotResponseModel>) {
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
          fieldData[PARAMETER] = stringOrEmpty(this[i].parameter)
          fieldData[MTD] = stringOrEmpty(this[i].mtdCount)
          fieldData[YTD_CNT] = stringOrEmpty(this[i].ytdCount)
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
        FragBusinessSnapshotTopBinding.inflate(inflater)
      val headerView = topBinding.root
      addView(headerView)
      if (arrayListClient.size > 0) {
        binding.tvNochart.visibility = View.GONE
        for (i in arrayListClient.indices) {
          val bottomBinding =
            FragBusinessSnapshotBottomBinding.inflate(inflater)
          val tableRow = bottomBinding.root
          bottomBinding.apply {
            tvFamilyStatus.text = arrayListClient[i][PARAMETER]
            tvMetMTD.text = arrayListClient[i][MTD]
            tvMetYTD.text = arrayListClient[i][YTD_CNT]
            val additionalParameters: MutableMap<String, String> = HashMap()
            tvMetMTD.setOnClickListener {
              additionalParameters.apply {
                putAll(getAdditionalParameterMap())
                this[COL_FILTER] = MTD_CNT
                this[ROW_FILTER] = tvFamilyStatus.text.toString()
                this[WIDGETTYPE] = tvFamilyStatus.text.toString()
              }
              if (tvMetMTD.text.toString() != "0") {
                navigateEvent.onNext(additionalParameters)
              }
            }

            tvMetYTD.setOnClickListener {
              additionalParameters.apply {
                clear()
                putAll(getAdditionalParameterMap())
                this[COL_FILTER] = YTD_CNT
                this[ROW_FILTER] = tvFamilyStatus.text.toString()
                this[WIDGETTYPE] = tvFamilyStatus.text.toString()
              }
              if (tvMetYTD.text.toString() != "0") {
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
    binding.progressBar.visibility = View.GONE
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.stop()
  }
}