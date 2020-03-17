package com.mosl.tabapp.notification

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragNotificationBinding
import com.mosl.tabapp.model.AccountDetails
import com.mosl.tabapp.notification.NotificationUiEvent.ItemClick
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NotificationFragmentKtt : CommonFragment(), NotificationView {

    private lateinit var binding: FragNotificationBinding

    @Inject
    internal lateinit var presenter: NotificationPresenter

    private val subject = PublishSubject.create<NotificationUiEvent>()

    private lateinit var notSection: DataManagerSection

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
        if (AccountDetails.isAufsb()) {
            binding.spCategory.setVisibility(View.GONE)
        }
    }

    private fun setUpRecyclerView() {
        notSection = DataManagerSection(onRetryClick)
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(notSection)
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

    companion object {
        var leadId = "0"
        var mUcid = "0"

        @JvmStatic
        fun newInstance(
            position: Int,
            type: String,
            leadID: String,
            ucid: String
        ): NotificationFragmentKtt {

            leadId = leadID
            mUcid = ucid
            val args = Bundle().apply {
                putInt("Position", position)
                putString("Type", type)
                putString("LeadID", leadID)
                putString(UCID, ucid)
            }
            return NotificationFragmentKtt().apply {
                arguments = args
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

        notSection.clearContent()

        uiModel.onSuccess {
            it.safeMap {
                notSection.add(NotificationItem(it))
            }

            if (it.size > 0) {
                binding.tvNochart.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.tvNochart.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }
}
