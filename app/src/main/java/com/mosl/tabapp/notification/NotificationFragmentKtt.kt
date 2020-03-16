package com.mosl.tabapp.notification

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragNotificationBinding
import com.mosl.tabapp.model.AccountDetails
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.model.NotificationModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NotificationFragmentKtt : CommonFragment(), OnItemSelectedListener, NotificationView {

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
//        val eventTypeArray = requireActivity().resources.getStringArray(R.array.NotificationCategory)
//        val arrCategory = ArrayList(Arrays.asList(*eventTypeArray))
//
//        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrCategory)
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        binding.spCategory.setAdapter(aa)


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

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun render(uiModel: UiModel<List<NotificationResponseData>>) {

        uiModel.onSuccess {
            it.safeMap {
                notSection.add(NotificationItem(it))
            }

        }

    }
}
