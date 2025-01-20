package com.mosl.tabapp.saathiIndirect.dashboard.foco

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightEvent.OnItemClicked
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightItem
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetPresenter
import com.mosl.tabapp.databinding.DialogFragmentActionableInsightsBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.Serializable
import javax.inject.Inject

class FocoActionableInsightDialogFragment @Inject constructor(val screenNavigator: ScreenNavigatorImpl): DialogFragment() {

  lateinit var binding: DialogFragmentActionableInsightsBinding

  private val dataActionSubject = PublishSubject.create<DataAction>()

  internal lateinit var section: DataManagerSection


  val onRetryClick: View.OnClickListener = View.OnClickListener {
    dataActionSubject.onNext(DataAction.Retry)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.dialog_fragment_actionable_insights, container, false)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = DialogFragmentActionableInsightsBinding.bind(view)
    setupViews()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  override fun onStart() {
    super.onStart()
    val window = dialog?.window
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  @SuppressLint("WrongConstant")
  private fun setupViews() {
    requireDialog().setCanceledOnTouchOutside(false)
    section = DataManagerSection(onRetryClick)
    setData()
    val context = requireContext()
    val actionableInsightAdapter = GroupAdapter<GroupieViewHolder>()
    actionableInsightAdapter.setOnItemClickListener { item, _ ->
      if (item is FocoActionableInsightItem) {
        val args = Bundle()
        args.putString(MOSLConstants.SYSTEM_NAME, item.data)
        screenNavigator.openPage(
          NavigationConstants.NAVIGATE_TO_INDIRECT_FOCO_DASHBOARD_REPORT.toString(), args, true,
          false
        )
        this.dismiss()
      }

    }
    actionableInsightAdapter.add(section)
    binding.rvActionableInsights.setHasFixedSize(true)
    binding.rvActionableInsights.layoutManager = LinearLayoutManager(context)
    binding.rvActionableInsights.addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    )
    binding.rvActionableInsights.adapter = actionableInsightAdapter

    binding.imgClose.setOnClickListener{
      dialog?.dismiss()
    }
  }

  fun setData() {
    val items = mutableListOf<Item<*>>()
    items.add(FocoActionableInsightItem(MOSLConstants.INDIRECT_TALK_TIME))
    items.add(FocoActionableInsightItem(MOSLConstants.INDIRECT_TOP_INCENTIVE))
    items.add(FocoActionableInsightItem(MOSLConstants.INDIRECT_CLIENT_REACTIVATION))
    items.add(FocoActionableInsightItem(MOSLConstants.INDIRECT_TOP_CLIENT_LIST))
     section.setContent(items)
  }
}