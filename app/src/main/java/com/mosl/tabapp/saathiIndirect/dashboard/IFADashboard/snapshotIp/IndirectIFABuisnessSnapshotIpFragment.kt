package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragBusinessSnapshotNewBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class IndirectIFABuisnessSnapshotIpFragment :CommonFragment(),IndirectIFABuisnessSnapshotIpView {
    private lateinit var binding: FragBusinessSnapshotNewBinding

    @Inject
    lateinit var presenter:IndirectIFABuisnessSnapshotIpPresenter
    private var model: WidgetModel = WidgetModel()
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFABusinessSnapshotIpTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    private val subject = IndirectIFADashboardUIEvent.getFundUiEvent()
    var strFilterVal:String=""
    override fun getLayout(): Int {
        return R.layout.frag_business_snapshot_new
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragBusinessSnapshotNewBinding.bind(view)
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }
    private fun setUpView() {

    }
}