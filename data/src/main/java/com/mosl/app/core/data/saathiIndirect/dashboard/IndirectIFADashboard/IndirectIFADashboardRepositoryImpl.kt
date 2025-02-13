package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard

import android.util.Log
import com.google.gson.Gson
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

class IndirectIFADashboardRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
) : IndirectIFADashboardRepository {
    override fun fnGetIFADashboardFilter(): Observable<IndirectFilterDataClass> {
        return saathiIndrectApi.getIFADashboard(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode)).map {
            IndirectIFADashboardMapper.fnTranformFilterList(it.response)
        }
    }

    override fun fnGetTodaysBirthdayAnniversary(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return  saathiIndrectApi.getIFABirthdayAnniversary(targetRequest).map {
            renderTable(Gson().toJson(it),false)
        }
    }

    override fun fnGetFirstMeetingNotDone(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASaleshFirstMeetingNotDone(targetRequest).map {
            renderTable(Gson().toJson(it),false)
        }
    }

    override fun fnGetLeadsNotUpdated(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFALeadNotUpdate(targetRequest).map {
            renderTable(Gson().toJson(it),false)
        }
    }

    override fun fnGetSalesDashInactive(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashInactive(targetRequest).map {
            renderTable(Gson().toJson(it),true)
        }
    }

    override fun fnGetIFASalesDashTeamAppointment(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashTeamAppointment(targetRequest).map {
            renderTable(Gson().toJson(it),false)
        }
    }

    override fun fnGetIFASalesDashNewLead(objIndirectIFADashboardInput: IndirectIFADashboardInput): Observable<DashboardReportCombo> {
        val targetRequest = objIndirectIFADashboardInput.requestTransform()
        return saathiIndrectApi.getIFASalesDashNewLead(targetRequest).map {
            renderTable(Gson().toJson(it),false)
        }
    }

    override fun fnGetIFASalesDashboBuisnessSnapshot(strFilterValue:String):  Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFASalesDashboBuisnessSnapshot(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency =strFilterValue ))
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFaSalesDashMyLead(strFilterValue: String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFaSalesDashMyLead(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, fitHierarchy = "Team"))
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetIFASalesDashMyBuisnessSnapIP(strFilterValue: String): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashMyBuisnessSnapIP(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency =strFilterValue ))
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetIFADashREvenueIP(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return saathiIndrectApi.getIFADashREvenueIP(req).map {
            mapTOIFADashboardModel(it)
        }
    }

    override fun fnGetIFADashRevenueBroking(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFADashRevenueBroking(req).map {
            mapTOIFADashboardModel(it)
        }
    }

    override fun fnGetIFADashMeeting(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFADashMeeting(req).map {
            mapToIFADashboardMeetingGraph(it)
        }
    }

    override fun fnGetIFADashIFAACtivation(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFADashIFAACtivation(req).map {
            mapTOIFADashboardModel(it)
        }
    }

    override fun fnGetIFASalesDashMeetingTeamUniqueGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
         return  saathiIndrectApi.getIFASalesDashMeetingTeamUniqueGraph(req).map {
             mapToFocoDashboardDetailsModel(it)
         }
    }

    override fun fnGetIFASalesDashMeetingTargetMetNotMetGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashMeetingTargetMetNotMetGraph(req).map {
            mapToFocoDashboardMyTargetMetNotModel(it)
        }
    }

    override fun fnGetIFASalesDashMeetingMyUniqueGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashMeetingMyUniqueGraph(req).map {
            mapToFocoDashboardMyUniqueDetailsModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueIPProductWiseSalesGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueIPProductWiseSalesGraph(req).map {
            mapToFocoDashboardRevenueIpProductwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueIPBranchWiseGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueIPBranchWiseGraph(req).map {
            mapToFocoDashboardRevenueIpBranchwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueIPTargetMetNotMetGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueIPTargetMetNotMetGraph(req).map {
            mapToFocoDashboardRevenueIpMetOrNotwiseModel(it)
        }
    }


    override fun fnGetIFASalesDashIFAActivationProductWiseGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashIFAActivationProductWiseGraph(req).map {
            mapToFocoDashboardRevenueIpProductwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashIFAActivationBranchWiseGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashIFAActivationBranchWiseGraph(req).map {
            mapToFocoDashboardRevenueIpBranchwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueBrokingProductWiseSalesGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueBrokingProductWiseSalesGraph(req).map {
            mapToFocoDashboardRevenueIpProductwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueBrokingBranchWiseGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueBrokingBranchWiseGraph(req).map {
            mapToFocoDashboardRevenueIpBranchwiseModel(it)
        }
    }

    override fun fnGetIFASalesDashRevenueBrokingTargetMetNotMetGraph(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  saathiIndrectApi.getIFASalesDashRevenueBrokingTargetMetNotMetGraph(req).map {
            mapToFocoDashboardRevenueIpMetOrNotwiseModel(it)
        }
    }
    override fun fnGetIFASalesDashRevenueBrokingHierarchy(strFilterValue: String): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashRevenueBrokingHierarchy(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, fitHierarchy = "Team")).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }
    override fun fnGetIFASalesDashRevenueBrokingDetail(strFilterValue: String,strFilterProduct:String): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashRevenueBrokingDetail(IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
            fltUserstatus = strFilterProduct,  calculatedPageP = 0, pageSizeP = 20, totalCountP = 0,
            pageP = 1,fltHierarchy = "Team", pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        ).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFASalesDashMeetingHierarchy(strFilterValue: String,strFilterProduct: String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFASalesDashMeetingHierarchy(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, category = strFilterProduct, fitHierarchy = "Team"))
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetIFASalesDashIFAActivationHierarchy(strFilterValue: String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.GetIFASalesDashIFAActivationHierarchy(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, fitHierarchy = "Team"))
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }
    override fun fnGetIFASalesDashMeetingDetail(strFilterValue: String,strFilterProduct:String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFASalesDashMeetingDetail(
            IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
            fltUserstatus = strFilterProduct, category ="Achieved", calculatedPageP = 0,  pageSizeP = 20, totalCountP = 0,
                pageP = 1,fltHierarchy = "Team", pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        )
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetIFASalesDashIFAActivationDetail(strFilterValue: String,strFilterProduct:String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFASalesDashIFAActivationDetail(
            IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
                fltUserstatus = strFilterProduct, category ="Achieved", calculatedPageP = 0,  pageSizeP = 20, totalCountP = 0,
                pageP = 1,fltHierarchy = "Team", pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        )
            .map {
                renderBuisnessSnapshotTable(Gson().toJson(it))
            }
    }

    override fun fnGetIFASalesDashRevenueIPDetail(strFilterValue: String,strFilterProduct:String): Observable<BusinessSnapshotCombo> {

        return  saathiIndrectApi.getIFASalesDashRevenueIPDetail(IndirectDetailsTablePostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue,
            fltUserstatus = strFilterProduct,  calculatedPageP = 0, pageSizeP = 20, totalCountP = 0,
            pageP = 1,fltHierarchy = "Team", pageSize = 20, page = 1, calculatedPage = 0, totalCount = 0)
        ).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fnGetIFASalesDashRevenueIPHierarchy(strFilterValue: String): Observable<BusinessSnapshotCombo> {
        return saathiIndrectApi.getIFASalesDashRevenueIPHierarchy(IndirectDashboardFilterPostRequest(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, fitHierarchy = "Team")).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fngetIFASalesDashMyLeadsDetail(objIndirectIFAMyLeadDetailsFilter: IndirectIFAMyLeadDetailsFilter): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashMyLeadsDetail(
            MyLeadDetailsRequestPost(
                employeeCode = userManager.getUser()?.employeeCode, fltUserstatus = objIndirectIFAMyLeadDetailsFilter.strSelFilterBy, category = objIndirectIFAMyLeadDetailsFilter.strSelCategory,
                calculatedPageP = 0, pageSizeP = 20, totalCountP = 0, pageP = 1, stageCode = objIndirectIFAMyLeadDetailsFilter.strSelStaqeCode, fltPeriod = objIndirectIFAMyLeadDetailsFilter.strSelPeriods,
                rowfilter = "Self", colfilter = objIndirectIFAMyLeadDetailsFilter.strSelStaqeCode, fltHierarchy = "Team", pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0
            )
        ).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fngetIFASalesDashFirstMeetingNotDoneDetail(objIndirectIFAMyLeadDetailsFilter: IndirectIFAMyLeadDetailsFilter): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashFirstMeetingNotDoneDetail(
            MyLeadDetailsRequestPost(
                employeeCode = userManager.getUser()?.employeeCode, frequency = null, fltUserstatus = objIndirectIFAMyLeadDetailsFilter.strSelFilterBy,
                category = null,product = null,parameter = null,date = null,filter = null,
                calculatedPageP = 0, pageSizeP = 20, totalCountP = 0, pageP = 1, stageCode = objIndirectIFAMyLeadDetailsFilter.strSelStaqeCode,days=objIndirectIFAMyLeadDetailsFilter.strSelPeriods,
                fltProduct = null, fltProspect = null, fltPeriod = null,rowfilter = null, colfilter = null, key1 = null,
                 fltHierarchy = "Team", pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0)
        ).map {
            renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }

    override fun fngetIFASalesDashLeadsNotUpdatedDetail(objIndirectIFAMyLeadDetailsFilter: IndirectIFAMyLeadDetailsFilter): Observable<BusinessSnapshotCombo> {
        return  saathiIndrectApi.getIFASalesDashLeadsNotUpdatedDetail(
            MyLeadDetailsRequestPost(
                employeeCode = userManager.getUser()?.employeeCode, frequency = null, fltUserstatus =objIndirectIFAMyLeadDetailsFilter.strSelFilterBy,
                category = null,product = null,parameter = null,date = null,filter = null,
                calculatedPageP = 0, pageSizeP = 20, totalCountP = 0, pageP = 1, stageCode = objIndirectIFAMyLeadDetailsFilter.strSelStaqeCode,days=objIndirectIFAMyLeadDetailsFilter.strSelPeriods,
                fltProduct = null, fltProspect = null, fltPeriod = null,rowfilter = null, colfilter = null, key1 = null,
                fltHierarchy = "Team", pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0)
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


    private fun renderTable(response: String?,isInActiveIFA:Boolean): DashboardReportCombo {
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
                    if(isInActiveIFA)
                    {
                        if(!header.equals("R_no",true) && !header.equals("Product",true)
                            && !header.equals("IsAdminMode",true) && !header.equals("Status",true)
                            && !header.equals("InApproval",true) && !header.equals("ApprovalFieldList",true)
                            && !header.equals("CreatedBy",true) && !header.equals("CreationDate",true)
                            && !header.equals("IFA_Code",true) && !header.equals("IFA_Name",true))
                        {

                            colHeaders.add(ColHeader(header))
                        }
                    }
                    else{
                        if(!header.equals("R_no",true) && !header.equals("Product",true)
                            && !header.equals("IsAdminMode",true) && !header.equals("Status",true)
                            && !header.equals("InApproval",true) && !header.equals("ApprovalFieldList",true)
                            && !header.equals("CreatedBy",true) && !header.equals("CreationDate",true))
                        {

                            colHeaders.add(ColHeader(header))
                        }
                    }

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
                            !header.equals("flg_empcode", ignoreCase = true)&& !header.equals("R_no",true) && !header.equals("Product",true)
                                && !header.equals("IsAdminMode",true) && !header.equals("Status",true)
                                && !header.equals("InApproval",true) && !header.equals("ApprovalFieldList",true)
                                && !header.equals("CreatedBy",true) && !header.equals("CreationDate",true)

                        ) {
                            headerData.add(header)
                            try {
                                if (header == "Completed" || header == "Postponed" || header == "Cancelled" || header == "Pending") {
                                    values.add(json.get(header).toString())
                                } else {
                                    if(!json.isNull(header))
                                    {
                                        val toDouble = json.get(header).toString().toDouble()
                                        if (toDouble == 0.0) {
                                            values.add("0")
                                        } else {
                                            if (json.get(header).toString().contains(".")) {
                                                if(json.get(header).toString().endsWith(".0"))
                                                {
                                                    values.add(json.get(header).toString().substringBefore("."))
                                                }
                                                else
                                                {
                                                    values.add(toDouble.toBigDecimal().toPlainString())
                                                }

                                            } else {
                                                values.add(json.get(header).toString())
                                            }

                                        }
                                    }
                                    else{

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