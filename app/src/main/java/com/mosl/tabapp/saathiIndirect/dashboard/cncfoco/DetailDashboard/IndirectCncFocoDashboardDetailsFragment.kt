package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCncAdminDashboardDetailsBinding
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardDetailsBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable.IndirectIFAUniqueReportHierchyTableFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable.IndirectIFAUniqueReportTableFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardView
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable.IndirectCncAdminUniqueReportTableFragment
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target.IndirectCncTargetView
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class IndirectCncFocoDashboardDetailsFragment:CommonFragment(),IndirectCncFocoDashboardDetailsView {
    internal lateinit var binding: FragIndirectCncAdminDashboardDetailsBinding
    @Inject
    lateinit var presenter:IndirectCncFocoDashboardDetailsPresenter
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private lateinit var filterAdapterCategory: ArrayAdapter<LookUp>
    private val subject = IndirectCncFocoDashboardDetailsUiEvent.getFundUiEvent()
    var strFilterVal:String=""
    var strWidgetType:String =""
    override fun getLayout() = R.layout.frag_indirect_cnc_admin_dashboard_details

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCncAdminDashboardDetailsBinding.bind(view)
        hideAppTitle()
        fnSetUpArgument()
        presenter.start()
        setupViews()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterPeriod.adapter = filterAdapter

        binding.spFilterPeriod.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                subject.onNext(IndirectCncFocoDashboardDetailsUiEvent.onFilterChanged(binding.spFilterPeriod.selectedItem.toString()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        });
        filterAdapterCategory = SpinnerAdapterFactory.getDefaultAdapter(requireContext())

        binding.spFiltercncAdmin.adapter = filterAdapterCategory
        var lstFilterCategory= mutableListOf<LookUp>()
        lstFilterCategory.add(LookUp(0,"Select"))
        lstFilterCategory.add(LookUp(1,"Active"))
        lstFilterCategory.add(LookUp(2,"InActive"))
        filterAdapterCategory.setSpinnerData(binding.spFiltercncAdmin, lstFilterCategory)
        binding.spFiltercncAdmin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                subject.onNext(IndirectCncFocoDashboardDetailsUiEvent.onFilterCategoryChanged(binding.spFiltercncAdmin.selectedItem.toString()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        });

        binding.ivBusinessSnapshotHeadReport.setOnClickListener {
            toggleSection(
                binding.llBusinessSnapshotHeadReport, binding.ivBusinessSnapshotHeadReport
            )
        }

        loadReporttable()

        if(strWidgetType.equals(IndirectCncTargetView.strReve))
        {
            binding.tvHeadTitle2.setText("Revenu Details")
        }
        else if(strWidgetType.equals(IndirectCncTargetView.strNoOfClient)){
            binding.tvHeadTitle2.setText(IndirectCncTargetView.strNoOfClient)
        }

    }

    private fun fnSetUpArgument()
    {
        strWidgetType= this.arguments?.getString(MOSLConstants.SYSTEM_NAME, "") ?: ""
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    override fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterPeriod, it.lstPeriods)

            it.lstPeriods.forEachIndexed { index, lookUp ->
                if(lookUp.lookUpName.equals(IndirectIFADashboardView.strSelectedFilter))
                {
                    binding.spFilterPeriod.setSelection(index)
                    return@forEachIndexed
                }
            }


            subject.onNext(IndirectCncFocoDashboardDetailsUiEvent.OnDetailsDataCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onFilterEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
    }

    override fun onDashboardDetailsEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.OnDetailsDataCall> {
        return  subject.ofType()
    }

   

    private  fun loadReporttable()
    {
        val args = Bundle()
        val leadsFragment = IndirectCncAdminUniqueReportTableFragment()
        args.putSerializable(MOSLConstants.SYSTEM_NAME, strWidgetType)
        leadsFragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llBusinessSnapshotHeadReport, leadsFragment)
            .commit()
    }

    private fun toggleSection(
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
}