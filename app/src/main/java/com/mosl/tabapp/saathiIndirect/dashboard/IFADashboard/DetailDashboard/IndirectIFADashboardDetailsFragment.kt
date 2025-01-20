package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.base.DashboardFragment
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardDetailsBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.HorinzatalIFAUniqueWidgetItem
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueUiEvent
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport.IndirectIFAUniqueView
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable.IndirectIFAUniqueReportHierchyTableFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable.IndirectIFAUniqueReportTableFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADashboardDetailsFragment:CommonFragment(),IndirectIFADashboardDetailsView {

    internal lateinit var binding:FragIndirectIfaDashboardDetailsBinding

    @Inject
    lateinit var presenter: IndirectIFADashboardDetailsPresenter
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private val subject = IndirectIFADashboardDetailsUiEvent.getFundUiEvent()
    var strFilterVal:String=""
    var strWidgetType:String =""

    override fun getLayout() = R.layout.frag_indirect_ifa_dashboard_details

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectIfaDashboardDetailsBinding.bind(view)
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
               // subject.onNext(IndirectIFADashboardUIEvent.onFilterChanged(binding.spFilterBank.selectedItem.toString()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        });

        binding.ivTeamUniqueAchievement.setOnClickListener {
            toggleSection(
                binding.llTeamUniqueAchievement, binding.ivTeamUniqueAchievement
            )
        }

        binding.ivBusinessSnapshotHeadHierchyReport.setOnClickListener {
            toggleSection(
                binding.llBusinessSnapshotHeadHierchyReport, binding.ivBusinessSnapshotHeadHierchyReport
            )
        }

        binding.ivBusinessSnapshotHeadReport.setOnClickListener {
            toggleSection(
                binding.llBusinessSnapshotHeadReport, binding.ivBusinessSnapshotHeadReport
            )
        }
        loadTeamTargetAchievementFragment()
        loadHierchyReportTable()
        loadReporttable()

        if(strWidgetType.equals(IndirectIFATargetView.strMeeting))
        {
            binding.tvHeadTitle1.setText("Hierarchy wise meetings")
            binding.tvHeadTitle2.setText("Meeting Details")
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strIFAActivation)){
            binding.tvHeadTitle1.setText("Hierarchy wise IFA Activation")
            binding.tvHeadTitle2.setText("FA Activation Details")
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strReveIP)){
            binding.tvHeadTitle1.setText("Hierarchy wise meetings")
            binding.tvHeadTitle2.setText("Revenue IP Details")
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strReveBro)){
            binding.tvHeadTitle1.setText("Hierarchy wise Broking")
            binding.tvHeadTitle2.setText("Revenue Broking Details")
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

    override fun fnRenderFilterList(uiModel: UiModel<List<LookUp>>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterPeriod, it)
            subject.onNext(IndirectIFADashboardDetailsUiEvent.OnDetailsDataCall)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onFilterEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
    }

    override fun onDashboardDetailsEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.OnDetailsDataCall> {
        return  subject.ofType()
    }

    private fun loadTeamTargetAchievementFragment() {
        val args = Bundle()
        val leadsFragment = IndirectIFAUniqueFragment()
        args.putSerializable(MOSLConstants.SYSTEM_NAME, strWidgetType)
        leadsFragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llTeamUniqueAchievement, leadsFragment)
            .commit()
    }

    private fun loadHierchyReportTable()
    {

        val args = Bundle()
        val leadsFragment = IndirectIFAUniqueReportHierchyTableFragment()
        args.putSerializable(MOSLConstants.SYSTEM_NAME, strWidgetType)
        leadsFragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llBusinessSnapshotHeadHierchyReport, leadsFragment)
            .commit()
    }

    private  fun loadReporttable()
    {
        val args = Bundle()
        val leadsFragment = IndirectIFAUniqueReportTableFragment()
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