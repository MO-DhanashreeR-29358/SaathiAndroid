package com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco.CncFocoDashboardMapper.mapToCncDashboardClientMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco.CncFocoDashboardMapper.mapToCncDashboardMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToIFADashboardMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.requestTransform
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.mobile.domain.snapshot.BusinessSnapshotData
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardCncAdminFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDetailsTablePostRequest
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class CncFocoRepositoryImpl @Inject constructor(
    val api: SaathiIndirectApi,
    val userManager: UserManagerImpl,
):CncFocoRepository {
    override fun fnGetCncDashboardFilter(): Observable<IndirectFilterDataClass> {

        return api.getCNCFocoDashFilter(IndirectDashboardCncAdminFilterPostRequest(
employeeCode = userManager.getUser()?.employeeCode
        )).map {
            CncFocoDashboardMapper.fnTranformFilterList(it.response)
        }
    }

    override fun fngetCNCFocoDashRevenueWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getCNCFocoDashRevenueWidgetData(req).map {
            mapToCncDashboardMeetingGraph(it)
        }
    }

    override fun fngetCNCFocoDashNoOfClientMeetingWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  api.getCNCFocoDashNoOfClientMeetingWidgetData(req).map {
            mapToCncDashboardClientMeetingGraph(it)
        }
    }

    override fun fnGetCNCFocoDashMeetingDetail(
        strFilterValue: String,
        strFilterProduct: String
    ): Observable<BusinessSnapshotCombo> {
        return api.getCNCFocoDashMeetingDetail(
            IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
                fltUserStatus = strFilterProduct, calculatedPageP = 0,  pageSizeP = 20, totalCountP = 0,
                pageP = 1, pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        ).map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetCNCFocoDashRevenueDetail(
        strFilterValue: String,
        strFilterProduct: String
    ): Observable<BusinessSnapshotCombo> {
        return api.getCNCFocoDashRevenueDetail(
            IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
                fltUserStatus = strFilterProduct, calculatedPageP = 0,  pageSizeP = 20, totalCountP = 0,
                pageP = 1, pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        ).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    private fun renderBuisnessSnapshotTable(response: String?): BusinessSnapshotCombo{
        if(response != null) {
            val jsonObject = JSONObject(response)
            val jsonArray =
                if (jsonObject.has("Response") && jsonObject.getJSONObject("Response")
                        .has("Data")
                ) {
                    jsonObject.getJSONObject("Response").getJSONArray("Data")
                } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<com.mosl.mobile.domain.snapshot.ColHeader> =
                    mutableListOf()
                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next()
                        .toString()
                    if(!header.equals("R_no",true) && !header.equals("Product",true)
                        && !header.equals("IsAdminMode",true) && !header.equals("Status",true)
                        && !header.equals("InApproval",true) && !header.equals("ApprovalFieldList",true)
                        && !header.equals("CreatedBy",true) && !header.equals("CreationDate",true)
                        && !header.equals("SelectedPeriod",true) && !header.equals("R",true)
                        && !header.equals("level_0_Name",true) && !header.equals("level_1_Name",true)
                        && !header.equals("level_2_Name",true) && !header.equals("level_3_Name",true)
                        && !header.equals("level_4_Name",true) && !header.equals("level_5_Name",true)
                        && !header.equals("level_6_Name",true) && !header.equals("level_7_Name",true)
                        && !header.equals("level_8_Name",true) && !header.equals("level_9_Name",true)
                        && !header.equals("level_10_Name",true))
                    {
                        colHeaders.add(com.mosl.mobile.domain.snapshot.ColHeader(header))
                    }
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<BusinessSnapshotData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    val values: ArrayList<String> = ArrayList()
                    val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
                    headers.forEach { header ->
                        if(json.isNull(header))
                        {
                            values.add("")
                        }
                        else{
                            if(json.get(header).toString().equals("0.0"))
                            {
                                values.add("0")
                            }
                            else if(json.get(header).toString().endsWith(".0"))
                            {
                                values.add(json.get(header).toString().substringBefore("."))
                            }
                            else{
                                values.add(json.get(header).toString())
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
                    data.add(BusinessSnapshotData(setData(values), headers,hasMap))
                }
                return BusinessSnapshotCombo(headers, data = data)
            } else {
                return  BusinessSnapshotCombo(emptyList(), data = emptyList())
            }
        }
        else{
            return   BusinessSnapshotCombo(emptyList(), data = emptyList())
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