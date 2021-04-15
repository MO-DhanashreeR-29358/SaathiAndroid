package com.mosl.tabapp.leadViewDetail.history

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryCombo
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryData
import com.mosl.tabapp.R
import com.mosl.tabapp.R.array
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentMappingHistoryBinding
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryTableMapper.map
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory.getDefaultAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.forEachIndexed
import kotlin.collections.map
import kotlin.collections.mutableListOf

class MappingHistoryFragment : CommonFragment(), MappingHistoryView {
    private lateinit var binding: FragmentMappingHistoryBinding

    @Inject
    lateinit var presenter: MappingHistoryPresenter
    lateinit var pager: ViewPager
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<MappingHistoryTableRow>

    override fun getLayout(): Int {
        return R.layout.fragment_mapping_history
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMappingHistoryBinding.bind(view)
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setUpView() {
        /*binding.nestedScrollView.apply {
          descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
          isFocusable = true
          isFocusableInTouchMode = true
          setOnTouchListener { v: View, event: MotionEvent? ->
            v.requestFocusFromTouch()
            false
          }
        }*/
        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = MappingHistoryTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<MappingHistoryTableRow>(dynamicHeaderRow)
                .apply {
//          viewProvider.rowClickListener = { row, view ->
//            val reportData = tableRowAdapter.getItem(row).data
//            navigateEvent.onNext(getAdditionalParameterMap(reportData.data[0]))
//          }
                }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)

        var eventTypeArray: Array<String> = arrayOf()
        if (!getLeadId().equals("0", ignoreCase = true) && !getUcId().equals(
                "0",
                ignoreCase = true
            )
        ) {
            eventTypeArray = requireContext()
                .resources
                .getStringArray(array.LeadClientHistory)
        } else if (!getLeadId().equals("0", ignoreCase = true)) {
            eventTypeArray = requireContext()
                .resources
                .getStringArray(array.LeadHistory)
        } else if (!getUcId().equals("0", ignoreCase = true)) {
            eventTypeArray = requireContext()
                .resources
                .getStringArray(array.ClientHistory)
        }
        val arrEventType =
            Arrays.asList(*eventTypeArray)
        val arrAdapEventType =
            getDefaultAdapter<String>(requireContext())
        binding.spHistory.adapter = arrAdapEventType
        arrAdapEventType.setSpinnerData(binding.spHistory, arrEventType)

    }

    override fun renderData(uiModel: UiModel<MappingHistoryCombo>) {
        binding.tableWrapper.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapper.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(getRefreshColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<MappingHistoryTableRow> =
                map(data.data)
            formatDate(data.data)
            binding.tableWrapper.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    private fun formatDate(data: List<MappingHistoryData>): List<MappingHistoryData> {
        val parentCopiedList = ArrayList(data)
        parentCopiedList.forEachIndexed { index, it ->
            run {
                if (it.data.size == LEAD_DETAIL_HISTORY_SIZE) {
                    val date = it.data[LEAD_DETAIL_ACTION_DATE_INDEX]
                    try {
                        if (date.isNotEmpty()) {
                            val copiedList: ArrayList<String> = ArrayList(it.data)
                            val formattedDate = CRMAppUtil.getDateFromString(
                                date,
                                DATE_FORMAT_TO_SERVER_MEETING,
                                CAL_DATE_FORMAT_WITH_TIME
                            )
                            copiedList[LEAD_DETAIL_ACTION_DATE_INDEX] = formattedDate.toString()
                            parentCopiedList[index].data = copiedList
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (it.data.size == LEAD_MAPPING_HISTORY_SIZE) {
                    val date = it.data[LEAD_MAPPING_ASSIGNED_DATE_INDEX]
                    val actionableDate = it.data[LEAD_MAPPING_ACTION_DATE_DATE_INDEX]
                    try {
                        if (date.isNotEmpty()) {
                            val copiedList: ArrayList<String> = ArrayList(it.data)
                            val formattedDate = CRMAppUtil.getDateFromString(
                                date,
                                DATE_FORMAT_TO_SERVER_MEETING,
                                CAL_DATE_FORMAT
                            )
                            copiedList[LEAD_MAPPING_ASSIGNED_DATE_INDEX] = formattedDate.toString()
                            parentCopiedList[index].data = copiedList
                        }
                        if (actionableDate.isNotEmpty()) {
                            val copiedList: ArrayList<String> = ArrayList(it.data)
                            val formattedDate = CRMAppUtil.getDateFromString(
                                actionableDate,
                                DATE_FORMAT_TO_SERVER_MEETING,
                                CAL_DATE_FORMAT_WITH_TIME
                            )
                            copiedList[LEAD_MAPPING_ACTION_DATE_DATE_INDEX] =
                                formattedDate.toString()
                            parentCopiedList[index].data = copiedList
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return parentCopiedList
    }

    override fun onEventTypeChange(): Observable<Int> {
        return binding.spHistory.itemSelections()
    }

    override fun getEventType(): String {
        return binding.spHistory.selectedItem as String
    }

    override fun getLeadId(): String {
        return arguments?.getString(LEAD_ID, "0") ?: "0"
    }

    override fun getUcId(): String {
        var mUcid = arguments?.getString(UCID, "0") ?: "0"
        mUcid = if (mUcid.isNullOrEmpty())
            "0"
        else mUcid
        return mUcid

    }

    fun setViewPager(viewPager: ViewPager) {
        pager = viewPager
    }

    companion object {
        @JvmStatic
        fun newInstance(
            position: Int, type: String?, leadID: String?,
            mUcid: String?
        ): MappingHistoryFragment {
            val fragment = MappingHistoryFragment()
            val args = Bundle()
            args.putInt("Position", position)
            args.putString("Type", type)
            args.putString(LEAD_ID, leadID)
            args.putString(UCID, mUcid)
            fragment.arguments = args
            return fragment
        }

        const val LEAD_MAPPING_HISTORY_SIZE = 10
        const val LEAD_DETAIL_HISTORY_SIZE = 13
        const val LEAD_DETAIL_ACTION_DATE_INDEX = 11
        const val LEAD_MAPPING_ASSIGNED_DATE_INDEX = 6
        const val LEAD_MAPPING_ACTION_DATE_DATE_INDEX = 9
    }

}
