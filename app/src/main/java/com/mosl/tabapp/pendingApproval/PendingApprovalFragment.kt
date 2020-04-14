package com.mosl.tabapp.pendingApproval

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.rx.ofType
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragPendingApprovalsBinding
import com.mosl.tabapp.notification.NotificationItem
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
      if (item is NotificationItem) {
        subject.onNext(ItemClick(item.getAdditionalData()))
      }
    }
  }

  override fun onItemClicked(): Observable<ItemClick> {
    return subject.ofType()
  }

  override fun render(uiModel: UiModel<List<NotificationResponseData>>) {
    TODO("Not yet implemented")
  }

}