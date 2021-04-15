package com.mosl.tabapp.notification

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.ErrorState.NO_DATA_ERROR
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragNotificationBinding
import com.mosl.tabapp.notification.NotificationUiEvent.ItemClick
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class NotificationFragment : CommonFragment(), NotificationView {

  private lateinit var binding: FragNotificationBinding

  @Inject
  internal lateinit var presenter: NotificationPresenter

  private val subject = PublishSubject.create<NotificationUiEvent>()

  private lateinit var section: DataManagerSection

  override fun getLayout(): Int {
    return R.layout.frag_notification
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragNotificationBinding.bind(view)

    setUpView()
    setUpRecyclerView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
      binding.spCategory.setVisibility(View.GONE)

  }

  private fun setUpRecyclerView() {
    section = DataManagerSection(onRetryClick)
      val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
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

  override fun onSpinnerSelection(): Observable<Int> {
    return binding.spCategory.itemSelections()
  }

  override fun getNotificationRequestCategory(): String {
    return binding.spCategory.selectedItem.toString()
  }

  override fun render(uiModel: UiModel<List<NotificationResponseData>>) {
    section.toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      section.showError(it)
    }
    uiModel.onSuccess {
      section.clearContent()
      if (it.isEmpty()) {
        section.showEmptyView(ErrorModel(ERROR_MSG, NO_DATA_ERROR))
      } else {
        section.setContent(it.safeMap { NotificationItem(it) })
      }
    }
  }

  companion object {
    const val ERROR_MSG = "You don\\'t have any notifications right now."
  }

}
