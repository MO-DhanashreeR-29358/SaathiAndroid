package com.mosl.app.core.data.reactivation

import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.mobile.domain.branch.BranchRepository
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.productivity.ProductivityCombo
import com.mosl.mobile.domain.productivity.ProductivityData
import com.mosl.mobile.domain.reactivation.ReactivationDetailCombo
import com.mosl.mobile.domain.reactivation.ReactivationDetailData
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.branch.BranchRequest
import com.mosl.saathi.api.model.reactivation.ReactivationDetailRequest
import com.mosl.saathi.api.model.reactivation.ReactivationSummaryRequest
import com.mosl.shared.util.toConvertDecimals
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class ReactivationRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManagerImpl
) : ReactivationRepository {

    override fun getReactivationSummaryForSales(period : String): Observable<ReactivationSummaryItem> {
        val request = ReactivationSummaryRequest(userManager.user.employeeCode, period)
        return saathiApi.getReactivationSummary(request).map {
            it.response?.data?.first()?.summaryItemResponseTransform()!!
        }
    }

    override fun getReactivationSummaryForTL(period: String): Observable<ReactivationSummaryItem> {
        val request = ReactivationSummaryRequest(userManager.user.employeeCode, period)
        return saathiApi.getReactivationSummaryForTL(request).map {
            it.response?.data?.first()?.summaryItemResponseTransform()!!
        }
    }

    override fun getReactivationDetailForSales(
        period: String,
        stage: String
    ): Observable<ReactivationDetailCombo> {
        val request = ReactivationDetailRequest(userManager.user.employeeCode, period, stage)
        return saathiApi.getReactivationDetail(request).map { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response").getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()
                val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next().toString()
                    colHeaders.add(ColHeader(header))
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

                    headers.forEach { header ->
                        values.add(json.get(header).toString())
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
        period: String, stage : String
    ): Observable<ReactivationDetailCombo> {
        val request = ReactivationDetailRequest(userManager.user.employeeCode, period, stage)
        return saathiApi.getReactivationAssignedHierarchyClientReportList(request).map { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = if (jsonObject.has("Response")) {
                jsonObject.getJSONObject("Response").getJSONArray("Data")
            } else null
            if (jsonArray != null && jsonArray.length() > 0) {
                val colHeaders: MutableList<ColHeader> = mutableListOf()
                val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
                val first = jsonArray.getJSONObject(0)
                val iterator = first.keys()
                while (iterator.hasNext()) {
                    val header = iterator.next().toString()
                    colHeaders.add(ColHeader(header))
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

                    headers.forEach { header ->
                        values.add(json.get(header).toString())
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