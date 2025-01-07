package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardMapper.requestTransform
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class IndirectIFADashboardRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
) : IndirectIFADashboardRepository {
    override fun fnGetIFADashboardFilter(): Observable<List<LookUp>> {
        return saathiIndrectApi.getIFADashboard(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode)).map {
            IndirectIFADashboardMapper.fnTranformFilterList(it.response?.lstPeriod)
        }
    }

    override fun fnGetTodaysBirthdayAnniversary(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return  saathiIndrectApi.getIFABirthdayAnniversary(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetFirstMeetingNotDone(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASaleshFirstMeetingNotDone(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetLeadsNotUpdated(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFALeadNotUpdate(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetSalesDashInactive(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashInactive(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFASalesDashTeamAppointment(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashTeamAppointment(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFASalesDashNewLead(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashNewLead(targetRequest).map {
            renderTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFASalesDashboBuisnessSnapshot(): Observable<String> {
        return saathiIndrectApi.getIFASalesDashboBuisnessSnapshot(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetIFASalesDashMyBuisnessSnapIP(): Observable<String> {
        return  saathiIndrectApi.getIFASalesDashMyBuisnessSnapIP(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            it.string()
        }
    }

    override fun fnGetIFADashREvenueIP(): Observable<String> {
        return saathiIndrectApi.getIFADashREvenueIP(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
          it.string()
        }
    }

    override fun fnGetIFADashRevenueBroking(): Observable<String> {
        return  saathiIndrectApi.getIFADashRevenueBroking(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            it.string()
        }
    }

    override fun fnGetIFADashMeeting(): Observable<String> {
        return  saathiIndrectApi.getIFADashMeeting(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            it.string()
        }
    }

    override fun fnGetIFADashIFAACtivation(): Observable<String> {
        return  saathiIndrectApi.getIFADashIFAACtivation(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            it.string()
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
                                    if(!json.isNull(header))
                                    {
                                        Log.d("Header Loop",json.get(header).toString());
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
                                    else{
                                        Log.d("Header Loop","else");
                                        values.add("")
                                    }


                                }

                            } catch (e: NumberFormatException) {
                                if(!json.isNull(header)){ values.add(json.get(header).toString()) }
                                else
                                {values.add("") }

                            }
                        }
                        if(!json.isNull(header)){
                            hasMap[header] = json.get(header)
                                .toString()
                        }
                        else{
                            hasMap[header] = ""
                        }

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