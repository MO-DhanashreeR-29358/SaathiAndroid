package com.mosl.tabapp.actionableinsight

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.tabapp.R
import com.mosl.tabapp.actionableinsight.ActionableInsightEvent.OnItemClicked
import com.mosl.tabapp.databinding.DialogFragmentActionableInsightsBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ActionableInsightDialogFragment : DialogFragment(), ActionableInsightView {

  lateinit var binding: DialogFragmentActionableInsightsBinding
  @Inject
  lateinit var presenter: ActionableInsightPresenter
  private val dataActionSubject = PublishSubject.create<DataAction>()
  private val actionableInsightEvent = PublishSubject.create<ActionableInsightEvent>()
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
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = DialogFragmentActionableInsightsBinding.bind(view)
    setupViews()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  override fun onStart() {
    super.onStart()
    val window = dialog?.window
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  private fun setupViews() {
    requireDialog().setCanceledOnTouchOutside(false)
    section = DataManagerSection(onRetryClick)

    val context = requireContext()
    val actionableInsightAdapter = GroupAdapter<ViewHolder>()
    actionableInsightAdapter.setOnItemClickListener { item, _ ->
      if (item is ActionableInsightItem) {
        actionableInsightEvent.onNext(ActionableInsightEvent.OnItemClicked(item.data!!))
      }
    }
    actionableInsightAdapter.add(section)
    binding.rvActionableInsights.setHasFixedSize(true)
    binding.rvActionableInsights.layoutManager = LinearLayoutManager(context)
    binding.rvActionableInsights.addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    )
    binding.rvActionableInsights.adapter = actionableInsightAdapter
  }

  override fun isDataEmpty(): Boolean {
    return section.isDataEmpty
  }

  override fun onDataAction(): Observable<DataAction> {
    return dataActionSubject
  }

  override fun onActionableInsightItemClick(): Observable<OnItemClicked> {
    return actionableInsightEvent.ofType()
  }

  override fun render(data: List<WidgetModel>) {
    val items = mutableListOf<Item<*>>()
    if (data.isNotEmpty()) {
      for (widgetModel in data) {
        items.add(ActionableInsightItem(widgetModel))
      }
      section.setContent(items)
    }
  }

  override fun onCloseClicked(): Observable<Unit> {
    return binding.imgClose.clicks()
  }

  override fun dismissDialog() {
    this.dismiss()
  }

  override fun onDestroyView() {
    presenter.start()
    super.onDestroyView()
  }

  companion object {
    const val ARG_LIST_DATA = "arg_data"
    const val ARG_DASHBOARD_FOR = "arg_dashboard_for"
  }
}