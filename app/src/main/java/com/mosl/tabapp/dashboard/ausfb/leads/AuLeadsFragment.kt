package com.mosl.tabapp.dashboard.ausfb.leads

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentAuLeadsBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class AuLeadsFragment : CommonFragment(), AuLeadsView {

    lateinit var binding: FragmentAuLeadsBinding
    lateinit var dashboardModel: DashboardModel
    private var widgetSystemName: String? = null
    private val addtionalParameterMap: Map<*, *> =
        HashMap<Any, Any>()
    private var filterFirst: String = ""
    private var filterSecond: String = ""
    private lateinit var monthAdapter: ArrayAdapter<FilterRowData>
    private lateinit var productAdapter: ArrayAdapter<FilterRowData>
    private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
    private lateinit var branchAdapter: ArrayAdapter<FilterRowData>
    private lateinit var subBranchAdapter: ArrayAdapter<FilterRowData>

    @Inject
    lateinit var presenter: AuLeadsPresenter

    override fun getLayout(): Int {
        return R.layout.fragment_au_leads
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuLeadsBinding.bind(view)
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)


    }

    private fun setUpView() {
        val context = requireContext()
        dashboardModel = arguments?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        val groupModel = dashboardModel.getGroupModel(
            DashboardSystemModel.getLeadSystemName()
        )
        val model: WidgetModel
        val widgetList = groupModel.widgetList
        model = if (widgetList.size == 1) {
            widgetList[0]
        } else {
            widgetList[1]
        }
        widgetSystemName = model.systemName
        val filterList = model.widgetFilterList
        if (filterList.size == 2) {
            monthAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterFirst.adapter = monthAdapter
            monthAdapter.setSpinnerData(binding.spFilterFirst, filterList[0].filterData)
            val productDataFirst =
                FilterRowData.getSpinnerFilterDataByValue(filterFirst, monthAdapter)
            if (productDataFirst != null) {
                binding.spFilterFirst.setSelection(productDataFirst.position)
            }
            productAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterSecond.adapter = productAdapter
            productAdapter.setSpinnerData(binding.spFilterSecond, filterList[1].filterData)
            binding.spFilterSecond.visibility = View.VISIBLE
            val productDataSecond =
                FilterRowData.getSpinnerFilterDataByValue(filterSecond, productAdapter)
            if (productDataSecond != null) {
                binding.spFilterSecond.setSelection(productDataSecond.position)
            }
        } else if (filterList.size == 1) {
            monthAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterFirst.adapter = monthAdapter
            monthAdapter.setSpinnerData(binding.spFilterFirst, filterList[0].filterData)
            val productDataFirst =
                FilterRowData.getSpinnerFilterDataByValue(filterFirst, monthAdapter)
            if (productDataFirst != null) {
                binding.spFilterFirst.setSelection(productDataFirst.position)
            }
        }
        bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterBank.adapter = bankAdapter

        branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterBranch.adapter = branchAdapter

        subBranchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterSubBranch.adapter = subBranchAdapter

    }

    override fun onMonthChange(): Observable<Int> {
        return binding.spFilterFirst.itemSelections()
    }

    override fun onProductChange(): Observable<Int> {
        return binding.spFilterSecond.itemSelections()
    }

    override fun onBankChange(): Observable<Int> {
        return binding.spFilterBank.itemSelections()
    }

    override fun onBranchChange(): Observable<Int> {
        return binding.spFilterBranch.itemSelections()
    }

    override fun onSubBranchChange(): Observable<Int> {
        return binding.spFilterSubBranch.itemSelections()
    }

    override fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            showOnFailurePopup(error)
        }

        uiModel.onSuccess {
            bankAdapter.setSpinnerData(binding.spFilterBank, it)
        }
    }
}