package com.mosl.app.core.data.saathiIndirect.dashboard

import android.util.Log
import com.google.gson.Gson
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodFilterInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import com.mosl.saathi.api.SaathiIndirectApi
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.emptyList
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.sortedBy

class FocoRepositoryImpl @Inject constructor(
    val api: SaathiIndirectApi,
    val gson: Gson
) : FocoRepository {


    override fun getPeriodFilter(request: FocoDashboardInput): Observable<List<FocoPeriodModel>> {
        val targetRequest = request.requestTransform()
        return api.getFocoPeriodFilter(targetRequest).map {
                it.data.list.itemTransform()
        }
    }

    override fun getActionableTalkTime(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getTalkTimeData(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getActionableTopIncentive(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getTopIncentiveData(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getActionableClientReactivation(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getClientReactivationData(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getActionableTopClientList(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getTopClientListData(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getFocoDashRevenueWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashRevenueWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashRevenueGrowthWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashRevenueGrowthWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashNoOfClientsWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashNoOfClientsWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashOnlineRevenueWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashOnlineRevenueWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashClientReachWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashClientReachWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashIPSalesWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashIPSalesWidgetData(req)
            .map {
                mapToFocoDashboardModel(it)
            }
    }

    override fun getFocoDashTotalMobAppWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getFocoDashTotalMobListWidgetData(req)
            .map {
                mapToTotalMobileLoggedIn(it)
            }
    }

    override fun getFocoDashRunrateForAdvisorWidgetData(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getRunrateForAdvisorData(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getRevenueHierarchy(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getRevenueHierarchy(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getRevenueDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getRevenueDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getClientMeetingHierarchy(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getClientMeetingHierarchy(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getClientMeetingDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getClientMeetingDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getOnlineRevenueDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getOnlineRevenueDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getClientReachDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getClientReachDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getIpSalesHierarchy(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getIpSalesHierarchy(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getIpSalesDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getIpSalesDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    override fun getTotalMobAppDetail(request: FocoDashboardInput): Observable<DashboardReportCombo> {
        val req = request.requestTransform()
        return api.getTotalMobAppDetail(req)
            .map {
                renderTable(Gson().toJson(it))
            }
    }

    private fun renderTable(response: String?): DashboardReportCombo {
        if(response != null) {
            Log.d("dashboardReportRes", response)
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
                        if (!header.equals("WidgetSystemName", ignoreCase = true) &&
                            !header.equals("flg_empcode", ignoreCase = true)
                        ) {
                            headerData.add(header)
                            try {
                                if (header == "Completed" || header == "Postponed" || header == "Cancelled" || header == "Pending") {
                                    values.add(json.get(header).toString())
                                } else {
                                    val toDouble = json.get(header).toString().toDouble()
                                    if (toDouble == 0.0) {
                                        values.add("")
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