package com.mosl.tabapp.pendingApproval

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.pendingapproval.PendingApprovalData
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragPendingApprovalsBinding
import com.mosl.tabapp.pendingApproval.PendingApprovalUiEvent.ItemClick
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PendingApprovalFragment : CommonFragment(), PendingApprovalView {

  private lateinit var binding: FragPendingApprovalsBinding

  @Inject
  internal lateinit var presenter: PendingApprovalPresenter

  private val subject = PublishSubject.create<PendingApprovalUiEvent>()

  private lateinit var section: DataManagerSection

  override fun getLayout(): Int {
    return R.layout.frag_pending_approvals
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragPendingApprovalsBinding.bind(view)
    setUpView()
    setUpRecyclerView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    binding.tvApproved.setText(getString(R.string.txt_label_approved))
    binding.tvPendingApproval.setText(getString(R.string.txt_label_pending_approvals))
  }

  private fun setUpRecyclerView() {
    section = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>().apply {
      add(section)
    }

    binding.recyclerView.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = groupAdapter
      setHasFixedSize(true)
    }

    groupAdapter.setOnItemClickListener { item, _ ->
      if (item is PendingApprovalItem) {
        subject.onNext(ItemClick(item.getEntityField()))
      }
    }
  }

  override fun onItemClicked(): Observable<ItemClick> {
    return subject.ofType()
  }

  override fun render(uiModel: UiModel<PendingApprovalData>) {
    section.toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      section.showError(it)
    }
    uiModel.onSuccess {
      section.clearContent()
      if (it.listOfApprovals.isNotEmpty()) {
        section.setContent(it.listOfApprovals.safeMap { PendingApprovalItem(it) })
      }

      binding.tvApprovedValue.text = stringOrZero((it.summaryCount.approvedCount).toString())
      binding.tvPendingApprovalValue.text = stringOrZero(it.summaryCount.pendingCount.toString())
    }
  }

}