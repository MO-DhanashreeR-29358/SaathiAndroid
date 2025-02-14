package com.mosl.app.core.data.advanceSearch

import android.util.Log
import com.google.gson.Gson
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchInput
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchRepository
import com.mosl.mobile.domain.snapshot.AdvanceSearchData
import com.mosl.mobile.domain.snapshot.AdvanceSearchDataCombo
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.search.AdvanceSearchRequestPost
import com.mosl.shared.util.AppDateFormats
import com.mosl.shared.util.getFormatedDate
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import timber.log.Timber
import java.text.DateFormat
import javax.inject.Inject

class AdvanceSearchRepositoryImp @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManagerImpl
) :AdvanceSearchRepository {
    override fun fnGetAdvanceSearch(objAdvanceSearchInput: AdvanceSearchInput): Observable<AdvanceSearchDataCombo> {
     return  saathiApi.fnSearchClient(AdvanceSearchRequestPost(
leadID = objAdvanceSearchInput.leadId,
         clientCode = objAdvanceSearchInput.clientCode,
         mobile = objAdvanceSearchInput.mobileNo,
         email = objAdvanceSearchInput.emailId,
         name = objAdvanceSearchInput.name,
         aadhaarNumber = objAdvanceSearchInput.aadharNo,
         pANNumber = objAdvanceSearchInput.panNo,
         uCID = objAdvanceSearchInput.ucId,
         familyUCID = objAdvanceSearchInput.fucId,
         searchTypeId = objAdvanceSearchInput.searchTypeId
        )).map {
         renderBuisnessSnapshotTable(Gson().toJson(it),objAdvanceSearchInput.searchTypeId)
        }
    }



    private fun renderBuisnessSnapshotTable(response: String?,searchTypeId:Int?): AdvanceSearchDataCombo{
        try {
            if (response != null) {
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
                        val header = iterator.next().toString()
                        if (header.equals("Stage", true)
                            || header.equals("InterestedIn", true) || header.equals("Source", true)
                            || header.equals("ProspectSource", true) || header.equals("City", true)
                            || header.equals("Profile", true) || header.equals("AadhaarNumber", true)
                            || header.equals("PAN", true) || header.equals("Status", true)
                            || header.equals("AssignedTo", true)
                            || header.equals("ProductName", true) || header.equals(
                                "CreationDate",
                                true
                            )
                        ) {
                            colHeaders.add(com.mosl.mobile.domain.snapshot.ColHeader(header))
                        }
                    }
                    if(searchTypeId==1)
                    {
                        colHeaders.add(0, com.mosl.mobile.domain.snapshot.ColHeader("LeadID"))
                        colHeaders.add(1, com.mosl.mobile.domain.snapshot.ColHeader("Name"))
                        colHeaders.add(2, com.mosl.mobile.domain.snapshot.ColHeader("Mobile"))
                        colHeaders.add(3, com.mosl.mobile.domain.snapshot.ColHeader("Email"))
                    }
                    else
                    {
                        colHeaders.add(0, com.mosl.mobile.domain.snapshot.ColHeader("UCID"))
                        colHeaders.add(1, com.mosl.mobile.domain.snapshot.ColHeader("ClientCode"))
                        colHeaders.add(2, com.mosl.mobile.domain.snapshot.ColHeader("Name"))
                        colHeaders.add(3, com.mosl.mobile.domain.snapshot.ColHeader("Mobile"))
                        colHeaders.add(4, com.mosl.mobile.domain.snapshot.ColHeader("Email"))
                        colHeaders.add(5, com.mosl.mobile.domain.snapshot.ColHeader("EquityCode"))
                    }

                    val headers = colHeaders.map {
                        it.copy(index = response.indexOf(it.label))
                    }.map { it.label }

                    val data: ArrayList<AdvanceSearchData> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(i)
                        val values: ArrayList<Any> = ArrayList()
                        val hasMap: MutableMap<String, Any> = mutableMapOf<String, Any>()
                        headers.forEach { header ->
                            if (json.isNull(header)) {
                                values.add("")
                            } else {
                                if (json.get(header).toString().equals("0.0")) {
                                    values.add("0")
                                } else if (json.get(header).toString().endsWith(".0")) {
                                    values.add(json.get(header).toString().substringBefore("."))
                                } else {

                                    if (header.equals("CreationDate", true)) {
                                        val strformatDate = getFormatedDate(
                                            json.get(header).toString(),
                                            AppDateFormats.DF_JSON_DATE10,
                                            AppDateFormats.df_Date3
                                        )
                                        values.add(strformatDate)
                                    } else {
                                        values.add(json.get(header))
                                    }


                                }

                            }
                            if (!json.isNull(header)) {
                                hasMap[header] = json.get(header)
                                    .toString()
                            } else {
                                hasMap[header] = ""
                            }

                        }
                        data.add(AdvanceSearchData(setData(values), headers, hasMap))
                    }
                    return AdvanceSearchDataCombo(headers, data = data)
                } else {
                    return AdvanceSearchDataCombo(emptyList(), data = emptyList())
                }
            } else {
                return AdvanceSearchDataCombo(emptyList(), data = emptyList())
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()

        }
        return AdvanceSearchDataCombo(emptyList(), data = emptyList())
    }


    private fun setData(values: ArrayList<Any>): ArrayList<Any> {
        val list: ArrayList<Any> = ArrayList()
        list.add(values[0])
        for (i in 1 until values.size) {
            if (values[i] != "null" && values[i] != "")
                list.add(values[i])
            else if (values[i] == "null")
                list.add("")
            else list.add(values[i])
        }
        return list
    }
}