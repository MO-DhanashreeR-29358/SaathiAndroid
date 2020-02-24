package com.mosl.tabapp.dashboard.clients

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.clients.ClientItemResponseModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentClientsNewBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class ClientsFragmentNew : CommonFragment(), CliendsView {

    private lateinit var binding: FragmentClientsNewBinding

    @Inject
    lateinit var presenter: ClientsPresenter

    @Inject
    lateinit var dashboardModel: DashboardModel

    private lateinit var model: WidgetModel
    private var familyCount = 0
    private val arrayListClient =
        mutableListOf<HashMap<String, String>>()

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
        val context = requireContext()

        val groupModel = dashboardModel.getGroupModel(
            DashboardSystemModel.getClientSystemName()
        )
        val widgetList = groupModel.widgetList
        if (widgetList.size > 0) {
            model = widgetList[0]
        }
        val filterList = model.widgetFilterList
        lateinit var periodAdapter: ArrayAdapter<FilterRowData>
        if (filterList.size == 1) {
            periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spPeriod.adapter = periodAdapter
            periodAdapter.setSpinnerData(binding.spPeriod, filterList[0].filterData)

        }

    }

    override fun onPeriodChange(): Observable<Int> {
        return binding.spPeriod.itemSelections()
    }

    override fun getPeriod(): FilterRowData? {
        return binding.spPeriod.selectedItem as FilterRowData
    }

    override fun getWidgetSystemsName(): String? {
        return model.systemName
    }

    override fun getAddtionalParameterMap(): Map<String, String> {
        val addtionalParameterMap: MutableMap<String, String> = HashMap()

        addtionalParameterMap.apply {

            getPeriod()?.let {
                this[it.keyValue] = it.value
            }

            this[MOSLConstants.SELF_KEY] = dashboardModel.dashboardFor
        }

        return addtionalParameterMap
    }

    override fun renderData(uiModel: UiModel<ClientItemResponseModel>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure { error ->
            showOnFailurePopup(error)
            clientHideProgress()
            arrayListClient.clear()
            binding.tvNochart.setVisibility(View.VISIBLE)
        }

        uiModel.onSuccess {
            clientHideProgress()
            it.table1?.apply {
                for (i in this.indices) {
                    familyCount = this.get(i).count
                    break
                }
            }

            it.table?.apply {
                for (i in this.indices) {
                    val fieldData =
                        HashMap<String, String>()
                    fieldData[FAMILYSTATUS] = this.get(i).familyStatus
                    fieldData[DAY] = this.get(i).dayCount
                    fieldData[MTD] = this.get(i).mtdCount
                    fieldData[YTD_CNT] = this.get(i).ytdCount
                    arrayListClient.add(fieldData)
                }
            }

        }
    }

    override fun getClientList(): MutableList<HashMap<String, String>> {
        return arrayListClient
    }

    override fun getFamilyCount(): Int {
        return familyCount
    }

    private fun clientHideProgress() {
        binding.progressBar.setVisibility(View.GONE)
    }

    companion object {
        const val FAMILYSTATUS = "FAMILYSTATUS";
        const val DAY = "Day";
        const val MTD = "MTD";
        const val YTD_CNT = "YTD_CNT";
    }

}