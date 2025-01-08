package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamWidgetUiEvent
import com.mosl.tabapp.databinding.FragIndirectIfaTargetBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import com.mosl.commons.rx.ofType
import timber.log.Timber
import javax.inject.Inject

class IndirectIFATargetFragment:CommonFragment(),IndirectIFATargetView {

    internal lateinit var binding:FragIndirectIfaTargetBinding

    @Inject
    lateinit var presenter: IndirectIFATragetPresenter
    private var itemList = mutableListOf<HorinzatalIFATargetWidgetItem>()
    private val widgetUiEvent =
        PublishSubject.create<TeamWidgetUiEvent>()

    private val subject = IndirectIFADashboardUIEvent.getFundUiEvent()
    var strFilterVal:String=""
    override fun getLayout() = R.layout.frag_indirect_ifa_target

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectIfaTargetBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.horizontalRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = groupAdapter
        }
        val objWidgetMode:WidgetModel= WidgetModel()
        objWidgetMode.name="Meeting"
        objWidgetMode.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode.isSelected=true
        itemList.add(HorinzatalIFATargetWidgetItem(objWidgetMode, widgetUiEvent))

        val objWidgetMode2:WidgetModel=WidgetModel()
        objWidgetMode2.name="IFA Activation"
        objWidgetMode2.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode2.isSelected=false
        itemList.add(HorinzatalIFATargetWidgetItem(objWidgetMode2, widgetUiEvent))

        val objWidgetMode3:WidgetModel=WidgetModel()
        objWidgetMode3.name="Revenu-IP"
        objWidgetMode3.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode3.isSelected=false
        itemList.add(HorinzatalIFATargetWidgetItem(objWidgetMode3, widgetUiEvent))


        val objWidgetMode4:WidgetModel=WidgetModel()
        objWidgetMode4.name="Revenu-Broking"
        objWidgetMode4.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode4.isSelected=false
        itemList.add(HorinzatalIFATargetWidgetItem(objWidgetMode4, widgetUiEvent))

    groupAdapter.addAll(itemList)
    }

    override fun onFilterEventCalled(): Observable<IndirectIFADashboardUIEvent.onFilterChanged> {
                return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
      this.strFilterVal=strFilterVal
        Log.d("my target",this.strFilterVal)
    }


}