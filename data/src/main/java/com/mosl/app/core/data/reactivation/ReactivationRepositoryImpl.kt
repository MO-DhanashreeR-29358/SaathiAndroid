package com.mosl.app.core.data.reactivation

import com.google.gson.Gson
import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.mobile.domain.branch.BranchRepository
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.productivity.ProductivityCombo
import com.mosl.mobile.domain.productivity.ProductivityData
import com.mosl.mobile.domain.reactivation.*
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.branch.BranchRequest
import com.mosl.saathi.api.model.reactivation.*
import com.mosl.shared.util.toConvertDecimals
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class ReactivationRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManagerImpl
) : ReactivationRepository {

    override fun getReactivationSourceList(): Observable<List<String>> {
        val request = ReactivationSourceRequest(userManager.user.employeeCode)
        return saathiApi.getReactivationSource(request).map {
            it.response?.transformSourceList()!!
        }
    }

    override fun getReactivationSummaryForSales(period : String, source : String): Observable<ReactivationSummaryItem> {
        val request = ReactivationSummaryRequest(userManager.user.employeeCode, period, source)
        return saathiApi.getReactivationSummary(request).map {
            it.response?.data?.first()?.summaryItemResponseTransform()!!
        }
    }

    override fun getReactivationSummaryForTL(period: String, source : String): Observable<ReactivationSummaryItem> {
        val request = ReactivationSummaryRequest(userManager.user.employeeCode, period, source)
        return saathiApi.getReactivationSummaryForTL(request).map {
            it.response?.data?.first()?.summaryItemResponseTransform()!!
        }
    }

    override fun getReactivationDetailForSales(
        period: String,
        stage: String,
        source : String
    ): Observable<ReactivationDetailCombo> {
        val request = ReactivationDetailRequest(userManager.user.employeeCode, period, stage, source)
        return saathiApi.getReactivationDetail(request).map { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response").getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()

                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next().toString()
                    if(header == "BranchCode" || header == "Export" ||
                        header == "Period" || header == "IsAdminMode" || header == "Status" ||
                        header == "InApproval" || header == "ApprovalFieldList"
                        || header == "EmployeeCode" || header == "FirstReportingTo"){
                    }else{
                        colHeaders.add(ColHeader(header))
                    }
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<ReactivationDetailData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    val values: ArrayList<String> = ArrayList()
                    val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()

                    headers.forEach { header ->
                        if(header == "IsEditable"){
                            if(json.get(header).toString().equals("True", ignoreCase = true)){
                                values.add("Edit")
                            }else{
                                values.add(" ")
                            }
                        }else{
                            values.add(json.get(header).toString())
                        }
                        hasMap[header] = json.get(header)
                            .toString()
                    }
                    data.add(ReactivationDetailData(setData(values), hasMap))
                }
                ReactivationDetailCombo(headers, data = data)

            } else {
                ReactivationDetailCombo(emptyList(), data = emptyList())
            }
        }
    }

    override fun getReactivationDetailForTL(
        employeeCode: String,
        firstReportingTo: String,
        period: String,
        stage: String, source : String
    ): Observable<ReactivationDetailCombo> {
        val request = ReactivationDetailTLRequest(employeeCode,firstReportingTo, period, stage, source)
        return saathiApi.getReactivationDetailTL(request).map { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response").getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()

                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next().toString()
                    if(header == "BranchCode" || header == "Export" ||
                        header == "Period" || header == "IsAdminMode" || header == "Status" ||
                        header == "InApproval" || header == "ApprovalFieldList"
                        || header == "EmployeeCode" || header == "FirstReportingTo"){
                    }else{
                        colHeaders.add(ColHeader(header))
                    }
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<ReactivationDetailData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    val values: ArrayList<String> = ArrayList()
                    val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()

                    headers.forEach { header ->
                        if(header == "IsEditable"){
                            if(json.get(header).toString().equals("True", ignoreCase = true)){
                                values.add("Edit")
                            }else{
                                values.add(" ")
                            }
                        }else{
                            values.add(json.get(header).toString())
                        }
                        hasMap[header] = json.get(header)
                            .toString()
                    }
                    data.add(ReactivationDetailData(setData(values), hasMap))
                }
                ReactivationDetailCombo(headers, data = data)

            } else {
                ReactivationDetailCombo(emptyList(), data = emptyList())
            }
        }
    }

    override fun getReactivationAssignedHierarchyClientReportList(
        period: String, stage : String, source : String
    ): Observable<ReactivationDetailCombo> {
        val request = ReactivationDetailRequest(userManager.user.employeeCode, period, stage, source)
        return saathiApi.getReactivationAssignedHierarchyClientReportList(request).map { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response").getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()

                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next().toString()
                    if(header == "BranchCode" || header == "Export" || header == "Level" ||
                        header == "IsAdminMode" || header == "Status" ||
                        header == "InApproval" || header == "ApprovalFieldList" ){
                    }else{
                        colHeaders.add(ColHeader(header))
                    }
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<ReactivationDetailData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    val values: ArrayList<String> = ArrayList()
                    val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()

                    headers.forEach { header ->
                        if(header == "IsEditable"){
                            if(json.get(header).toString().equals("True", ignoreCase = true)){
                                values.add("Edit")
                            }else{
                                values.add(" ")
                            }
                        }else{
                            values.add(json.get(header).toString())
                        }
                        hasMap[header] = json.get(header)
                            .toString()
                    }
                    data.add(ReactivationDetailData(setData(values), hasMap))
                }
                ReactivationDetailCombo(headers, data = data)

            } else {
                ReactivationDetailCombo(emptyList(), data = emptyList())
            }
        }
    }

    override fun getReactivationFeedback(request: ReactivationFeedbackItem): Observable<ReactivationFeedbackResponse> {
        val requestapi=ReactivationFeedbackRequest(request.employeeCode,request.period,request.currentStage,request.refNo,request.remarks)
        return  saathiApi.getFeedbackSubmit(requestapi).map { response->
            ReactivationFeedbackResponse(response.status,response.message)
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