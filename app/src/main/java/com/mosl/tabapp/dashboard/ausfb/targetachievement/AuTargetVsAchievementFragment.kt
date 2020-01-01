package com.mosl.tabapp.dashboard.ausfb.targetachievement

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.AusfbLeadModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.SubBranchCodeModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsPresenter
import com.mosl.tabapp.databinding.FragmentAuTargetVsAchievementBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.Observable
import javax.inject.Inject

class AuTargetVsAchievementFragment : CommonFragment(), AuTargetVsAchievementView {

    lateinit var binding: FragmentAuTargetVsAchievementBinding

    @Inject
    lateinit var presenter: AuLeadsPresenter

    @Inject
    lateinit var dashboardModel: DashboardModel

    private lateinit var model: WidgetModel


    override fun getLayout(): Int {
        return R.layout.fragment_au_leads
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuTargetVsAchievementBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)


    }


    override fun onPeriodChange(): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProductChange(): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBankChange(): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBranchChange(): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubBranchChange(): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderSubBranchFilter(uiModel: UiModel<List<SubBranchCodeModel>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBank(): BankCodeModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBranch(): BranchCodeModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubBranch(): SubBranchCodeModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPeriod(): FilterRowData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProduct(): FilterRowData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWidgetSystemsName(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderData(uiModel: UiModel<AusfbLeadModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}