package com.mosl.tabapp.dashboard.report.assign

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentActivity
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.dimen
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.LAST_CONNECTED
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.LEAD_STATUS
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.NAME
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.PROSPECT_ID
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.REGISTRATION_DATE
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.STAGE
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.TEAM_LEADER
import com.mosl.tabapp.databinding.*
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter
import io.reactivex.subjects.PublishSubject
import java.util.*

class DashboardReportAssignTableAdapter(
    val requireActivity: FragmentActivity,
    val publicSubject: PublishSubject<DashboardReportAssignUIEvent>
) : BaseTableAdapter() {
    private var commonHeaders: Array<CommonHeader>
    private val headers = arrayOf(
        NAME, PROSPECT_ID, REGISTRATION_DATE
    )
    private var isAllChecked = false
    private var isSingleAllChecked = false
    private val selectedRowList =
        ArrayList<String>()
    private val selectedRowMap: MutableMap<Int, Boolean> =
        HashMap()

    override fun getRowCount(): Int {
        return commonHeaders[0].list.size
    }

    fun clearList() {
        selectedRowList.clear()
        selectedRowMap.clear()
        isAllChecked = false
        isSingleAllChecked = false
    }

    fun clearReport() {
        commonHeaders[0].list.clear()
    }

    fun add(row: HashMap<String, String>) {
        commonHeaders[0].list.add(
            Item(
                row[NAME]!!,
                row[PROSPECT_ID]!!,
                row[REGISTRATION_DATE]!!,
                row[LAST_CONNECTED]!!,
                row[STAGE]!!,
                row[LEAD_STATUS]!!,
                row[TEAM_LEADER]!!
            )
        )
    }

    override fun getColumnCount(): Int {
        return headers.size
    }

    override fun getView(
        row: Int,
        column: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return when (getItemViewType(row, column)) {
            0 -> getFirstHeader(column, requireActivity)
            1 -> getHeader(column, requireActivity)
            2 -> getFirstBody(row, column, requireActivity)
            3 -> getBody(row, column, requireActivity)
            4 -> getFamilyView(row, column, requireActivity)
            else -> throw RuntimeException("runtime exception")
        }
    }

    //click on symbol header
    private fun getFirstHeader(
        column: Int,
        context: FragmentActivity
    ): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemTableHeaderFirstPendingBinding.inflate(inflater)
        binding.text1.text = headers[0]
        binding.cbClientName.isChecked = isAllChecked
        binding.cbClientName.setOnClickListener { view: View ->
            isAllChecked = (view as CheckBox).isChecked
            for (ctr in 0 until rowCount) {
                val approvalRequestId = getDevice(ctr - 1).data[1]
                selectedRowMap[ctr] = view.isChecked
                if (binding.cbClientName.isChecked) {
                    selectedRowList.add(approvalRequestId)
                    isSingleAllChecked = false
                } else {
                    selectedRowList.remove(approvalRequestId)
                }
            }
            publicSubject.onNext(
                DashboardReportAssignUIEvent.CheckBoxSelectedArrayList(
                    selectedRowList
                )
            )
            notifyDataSetChanged()
        }
        if (selectedRowList.isEmpty() || isSingleAllChecked) {
            binding.cbClientName.isChecked = false
        }

        return binding.root
    }


    //click on header other than symbol
    private fun getHeader(
        column: Int,
        context: FragmentActivity
    ): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemTableHeaderBinding.inflate(inflater)
        binding.text1.text = headers[column]
        return binding.root
    }

    private fun getFirstBody(
        row: Int,
        column: Int,
        context: FragmentActivity
    ): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemTableFirstPendingBinding.inflate(inflater)
        binding.itemTableFirst.setBackgroundResource(
            if (row % 2 == 0) color.alternateBackground else color.white
        )
        binding.tvTableFirst.text =
            getDevice(row).data[column + 1] //row= 1 to ... & col=-1
        var isChecked = selectedRowMap[row]
        if (isChecked == null) {
            isChecked = false
        }
        val approvalRequestId = getDevice(row).data[column + 2]
        if (isChecked) {
            binding.cbClientNameBody.isChecked = true
        } else {
            selectedRowList.remove(approvalRequestId)
            binding.cbClientNameBody.isChecked = false
        }
        binding.cbClientNameBody.setOnClickListener { v: View ->
            val isChecked1 = (v as CheckBox).isChecked
            selectedRowMap[row] = isChecked1
            if (isChecked1) {
                selectedRowList.add(approvalRequestId)
            } else {
                selectedRowList.remove(approvalRequestId)
                isSingleAllChecked = true
            }
            publicSubject.onNext(
                DashboardReportAssignUIEvent.CheckBoxSelectedArrayList(
                    selectedRowList
                )
            )
            notifyDataSetChanged()
        }
        return binding.root
    }

    private fun getBody(
        row: Int,
        column: Int,
        context: FragmentActivity
    ): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemTableBinding.inflate(inflater)
        binding.itemTable.setBackgroundResource(
            if (row % 2 == 0) color.alternateBackground else color.white
        )
        binding.text1.text =
            getDevice(row).data[column]
        binding.text1.setBackgroundResource(0)
        return binding.root
    }

    private fun getFamilyView(
        row: Int,
        column: Int,
        context: FragmentActivity
    ): View {

        val inflater = LayoutInflater.from(context)
        val binding = ItemTableFamilyBinding.inflate(inflater)

        val string: String = if (column == -1) {
            getFamily(row).name
        } else {
            ""
        }
        binding.text1.text = string
        return binding.root
    }

    override fun getWidth(column: Int): Int {
        var widthSize = 0
        if (column == -1) {
            widthSize = requireActivity.resources.getDimensionPixelSize(dimen._130sdp)
        }
        for (i in 0 until column) {
            widthSize = requireActivity.resources.getDimensionPixelSize(dimen._135sdp)
        }
        return widthSize
    }

    override fun getHeight(row: Int): Int {
        return if (row == -1) {
            requireActivity.resources.getDimensionPixelSize(dimen._40sdp)
        } else {
            requireActivity.resources.getDimensionPixelSize(dimen._40sdp)
        }
    }

    override fun getItemViewType(row: Int, column: Int): Int {
        return if (row == -1 && column == -1) {
            0
        } else if (row == -1) {
            1
        } else if (column == -1) {
            2
        } else {
            3
        }
    }

    private fun getFamily(row: Int): CommonHeader {
        var familyRow = row
        val family = 0
        while (row >= 0) {
            familyRow -= commonHeaders[family].size()
        }
        return commonHeaders[family]
    }

    private fun getDevice(row: Int): Item {
        var rowCount = row
        val family = 0
        while (rowCount >= 0) {
            rowCount -= commonHeaders[family].size()
        }
        return commonHeaders[family][rowCount + commonHeaders[family].size()]
    }

    override fun getViewTypeCount(): Int {
        return 5
    }

    init {
        commonHeaders =
            arrayOf(
                CommonHeader("asd")
            )
    }

    private inner class CommonHeader internal constructor(val name: String) {
        val list: MutableList<Item>
        fun size(): Int {
            return list.size
        }

        operator fun get(i: Int): Item {
            return list[i]
        }

        init {
            list =
                ArrayList()
        }
    }

    private inner class Item(
        name: String,
        prospectID: String,
        registrationDate: String,
        lastConnected: String,
        stage: String,
        leadStatus: String,
        teamLeader: String
    ) {
        val data: Array<String> = arrayOf(
            name, prospectID, registrationDate, lastConnected, stage, leadStatus, teamLeader
        )

    }
}