package com.mosl.tabapp.dashboard.ausfb.target

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.autarget.TableItemModel
import com.mosl.mobile.domain.autarget.TargetItemModel
import com.mosl.mobile.domain.bank.*
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentAuTargetVsAchievementBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class AuTargetFragment : CommonFragment(), AuTargetView {

    lateinit var binding: FragmentAuTargetVsAchievementBinding

    private lateinit var periodAdapter: ArrayAdapter<FilterRowData>
    private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
    private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>
    private lateinit var subBranchAdapter: ArrayAdapter<SubBranchCodeModel>
    @Inject
    lateinit var presenter: AuTargetPresenter

    @Inject
    lateinit var dashboardModel: DashboardModel
    private var widgetModelList: MutableList<WidgetModel> = mutableListOf()

    private val widgetUiEvent =
        PublishSubject.create<AuWidgetUiEvent>()

    private val tabelUiEvent =
        PublishSubject.create<AuWidgetUiEvent>()

    private var previousPosition: Int = 0
    private var itemList = mutableListOf<TargetWidgetItem>()
    private var tableItemList = mutableListOf<TargetTabelItem>()

    private var widgetModel: WidgetModel? = null

    private val tabelGroupAdapter = GroupAdapter<ViewHolder>()

    override fun getLayout(): Int {
        return R.layout.fragment_au_target_vs_achievement
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuTargetVsAchievementBinding.bind(view)
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        if (itemList.size > 0) {
            widgetUiEvent.onNext(
                AuWidgetUiEvent.ItemClick(
                    itemList.get(0).widgetModel,
                    0
                )
            )
        }
    }

    private fun setUpView() {
        val groupAdapter = GroupAdapter<ViewHolder>()
        binding.horizontalRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = groupAdapter
        }

        binding.rvTabel.apply {
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    1,
                    GridLayoutManager.VERTICAL,
                    false
                )
            adapter = tabelGroupAdapter
        }

        val groupModel = dashboardModel.getGroupModel(
            DashboardSystemModel.getTargetAchivementSystemName()
        )
        val filterList = groupModel.groupFilterList
        if (filterList.size == 1) {
            periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
            binding.spFilterPeriod.adapter = periodAdapter
            periodAdapter.setSpinnerData(binding.spFilterPeriod, filterList[0].filterData)
            arguments?.let { binding.spFilterPeriod.setSelection(it.getInt(MOSLConstants.FILTERFIRST)) }

        }

        widgetModelList = Objects.requireNonNull(dashboardModel)
            .getGroupModel(DashboardSystemModel.getTargetAchivementSystemName())
            .widgetList
        if (widgetModelList.size < 3) {
            widgetModelList.add(WidgetModel())
            widgetModelList.add(WidgetModel())
        } else if (widgetModelList.size < 4) {
            widgetModelList.add(WidgetModel())
        }

        widgetModelList.mapIndexed { position: Int, model: WidgetModel ->

            itemList.add(TargetWidgetItem(model, widgetUiEvent, requireContext()))
        }

        groupAdapter.addAll(itemList)

        bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBank.adapter = bankAdapter

        branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBranch.adapter = branchAdapter

        subBranchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterSubBranch.adapter = subBranchAdapter


    }


    override fun onPeriodChange(): Observable<Int> {
        return binding.spFilterPeriod.itemSelections()
    }

    override fun onBankChange(): Observable<Int> {
        return binding.spFilterBank.itemSelections()
    }

    override fun getBank(): BankCodeModel? {
        return binding.spFilterBank.selectedItem as? BankCodeModel
    }

    override fun getBranch(): BranchCodeModel? {
        return binding.spFilterBranch.selectedItem as? BranchCodeModel
    }

    override fun getSubBranch(): SubBranchCodeModel? {
        return binding.spFilterSubBranch.selectedItem as? SubBranchCodeModel
    }

    override fun getPeriod(): FilterRowData? {
        return binding.spFilterPeriod.selectedItem as? FilterRowData
    }


    override fun getWidgetSystemsName(): String {
        return widgetModel?.systemName ?: ""
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
            chartClear()
            setDefaultValues(1)
        }

        uiModel.onSuccess {
            bankAdapter.setSpinnerData(binding.spFilterBank, it)

        }
    }

    override fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure { error ->
            showOnFailurePopup(error)
            chartClear()
            setDefaultValues(2)
        }

        uiModel.onSuccess {
            branchAdapter.setSpinnerData(binding.spFilterBranch, it)
        }
    }

    override fun renderSubBranchFilter(uiModel: UiModel<List<SubBranchCodeModel>>) {
        toggleProgress(uiModel.inProgress)

        val selectSubBranch = mutableListOf<SubBranchCodeModel>()
        selectSubBranch.add(0, SelectSubBranch)

        uiModel.onFailure { error ->
            showOnFailurePopup(error)
            chartClear()
            setDefaultValues(3)
        }

        uiModel.onSuccess {
            subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, it)
        }
    }

    fun setDefaultValues(dropdownTypes: Int) {
        val selectBank = mutableListOf<BankCodeModel>()
        selectBank.add(0, SelectBank)
        val selectBranch = mutableListOf<BranchCodeModel>()
        selectBranch.add(0, SelectBranch)
        val selectSubBranch = mutableListOf<SubBranchCodeModel>()
        selectSubBranch.add(0, SelectSubBranch)
        when (dropdownTypes) {
            1 -> {
                bankAdapter.setSpinnerData(binding.spFilterBank, selectBank)
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            2 -> {
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            3 -> {
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            else -> {
                bankAdapter.setSpinnerData(binding.spFilterBank, selectBank)
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
        }
    }

    fun chartClear() {

    }


    override fun renderData(uiModel: UiModel<TargetItemModel>) {
        toggleProgressBar(uiModel.inProgress)
        tableItemList.clear()
        tabelGroupAdapter.clear()
        uiModel.onFailure { error ->
            showOnFailurePopup(error)
        }

        uiModel.onSuccess {
            it.table?.apply {
                TabelView(this)

            }
        }


    }

    fun TabelView(table: List<TableItemModel>) {

        (binding.rvTabel.getLayoutManager() as GridLayoutManager).spanCount = gridSpanCount(table)

        val sortedList = table.sortedWith(compareBy { it.ord })
        sortedList.mapIndexed { index, tableItemModel ->
            tableItemList.add(
                TargetTabelItem(
                    tableItemModel, tabelUiEvent, requireContext(),
                    this@AuTargetFragment
                )
            )
        }
        tabelGroupAdapter.addAll(tableItemList)

    }

    fun gridSpanCount(table: List<TableItemModel>): Int {
        if (table.size >= 3) {
            return 3
        } else if (table.size == 2) {
            return 2
        } else {
            return 1
        }

    }

    override fun onWidgetItemClicked(): Observable<AuWidgetUiEvent.ItemClick> {
        return widgetUiEvent.ofType()
    }

    override fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int) {
        if (currentPosition != previousPosition) {
            itemList.get(currentPosition).updateItem(true)
            itemList.get(previousPosition).updateItem(false)
            previousPosition = currentPosition
        }
        binding.tvUniqueLeads.setText(widgetModel.widgetTypeList.get(0).getTitle())
        this.widgetModel = widgetModel
    }

    override fun getWidgetModel(): WidgetModel? {
        return widgetModel
    }


}