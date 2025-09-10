package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectCCDashboard

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.data.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCDashboardMapper.requestTransform
import com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardMapper.requestTransform
import com.mosl.app.core.data.saathiIndirect.dashboard.mapTOIFADashboardModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardDetailsModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardMyTargetMetNotModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardMyUniqueDetailsModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardRevenueIpBranchwiseModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardRevenueIpMetOrNotwiseModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToFocoDashboardRevenueIpProductwiseModel
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToIFADashboardMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.requestTransform
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.CCDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCDashboardRepository
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCFilterDataClass
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFAMyLeadDetailsFilter
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDetailsTablePostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.MyLeadDetailsRequestPost
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class IndirectCCDashboardRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
) : IndirectCCDashboardRepository {


    override fun fnGetCCDashboardFilter(): Observable<IndirectCCFilterDataClass> {
        return saathiIndrectApi.getCCDashboardFilter(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode))
            .map {
                IndirectCCDashboardMapper.fnTranformFilterList(it.response)
            }
    }

    override fun getMeetingSummary(request: CCDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return saathiIndrectApi.getCCDashMeetingSummary(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getMeetingReachSummary(request: CCDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return saathiIndrectApi.getCCDashMeetingReachSummary(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    private fun renderTable(response: String?): DashboardReportCombo {
        if(response != null) {
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response")
                    .getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()
                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next()
                        .toString()
                    colHeaders.add(ColHeader(header))
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<DashboardReportData> = ArrayList()
                val headerData = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    headerData.clear()
                    val json = jsonArray.getJSONObject(i)
                    val values: ArrayList<String> = ArrayList()
                    val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
                    headers.forEach { header ->
                        if (!header.equals("IsAdminMode", ignoreCase = true) &&
                            !header.equals("Status", ignoreCase = true) &&
                            !header.equals("InApproval", ignoreCase = true) &&
                            !header.equals("CreationDate", ignoreCase = true)
                        ) {
                            headerData.add(header)
                            try {
                                if (header == "Completed" || header == "Postponed" || header == "Cancelled" || header == "Pending") {
                                    values.add(json.get(header).toString())
                                } else {
                                    val toDouble = json.get(header).toString().toDouble()
                                    if (toDouble == 0.0) {
                                        values.add("0")
                                    } else {
                                        if (json.get(header).toString().contains(".")) {
                                            values.add(toDouble.toBigDecimal().toPlainString())
                                        } else {
                                            values.add(json.get(header).toString())
                                        }

                                    }
                                }

                            } catch (e: NumberFormatException) {
                                values.add(json.get(header).toString())
                            }
                        }
                        hasMap[header] = json.get(header)
                            .toString()
                    }

                    data.add(DashboardReportData(setData(values), headerData, hasMap))
                }
                return DashboardReportCombo(headerData, data = data)
            } else {
                return DashboardReportCombo(emptyList(), data = emptyList())
            }
        }else{
            return DashboardReportCombo(emptyList(), data = emptyList())
        }
    }

    private fun setData(values: ArrayList<String>): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        list.add(values[0])
        for (i in 1 until values.size) {
            if (values[i] != "null" && values[i] != "")
                list.add(values[i])
            else if (values[i] == "null")
                list.add(values[i].replace("null", ""))
            else list.add(values[i])
        }
        return list
    }
}