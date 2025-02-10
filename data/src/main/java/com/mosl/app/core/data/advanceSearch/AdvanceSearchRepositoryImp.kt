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
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
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
         familyUCID = objAdvanceSearchInput.fucId
        )).map {
         renderBuisnessSnapshotTable(Gson().toJson(it))
        }
    }



    private fun renderBuisnessSnapshotTable(response: String?): AdvanceSearchDataCombo{
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
                    if(!header.equals("Equity",true) && !header.equals("CallDate",true)
                        && !header.equals("CallTime",true) && !header.equals("ProductID",true)
                        && !header.equals("PAN",true) && !header.equals("EquityCode",true)
                        && !header.equals("UCID",true) && !header.equals("EquityAdvisor",true)
                        && !header.equals("ClientCode",true) && !header.equals("FamilyUCID",true)
                        && !header.equals("EncLeadID",true) && !header.equals("EncUCID",true)
                        && !header.equals("SourceID",true) && !header.equals("ProspectSourceID",true)
                        && !header.equals("InterestedInID",true) && !header.equals("LeadProfileID",true)
                        && !header.equals("StageCodeID",true) && !header.equals("StatusNameID",true)
                        && !header.equals("StateName",true) && !header.equals("StateID",true)
                        && !header.equals("CityID",true) && !header.equals("CityName",true)
                        && !header.equals("LeadTypeID",true) && !header.equals("MeetingID",true)
                        && !header.equals("ActionableID",true) && !header.equals("ClientProfileID",true)
                        && !header.equals("ClientProfile",true) && !header.equals("ProductCategoryID",true)
                        && !header.equals("ClientStatusID",true) && !header.equals("DPValueID",true)
                        && !header.equals("DPValue",true) && !header.equals("AumValue",true)
                        && !header.equals("AumID",true) && !header.equals("SourceList",true)
                        && !header.equals("ProspectSourceList",true) && !header.equals("InterestedInList",true)
                        && !header.equals("LeadProfileList",true) && !header.equals("StageCodeList",true)
                        && !header.equals("StatusNameList",true) && !header.equals("CityList",true)
                        && !header.equals("LeadTypeList",true) && !header.equals("MeetingCallList",true)
                        && !header.equals("ActionablesList",true) && !header.equals("ClientProfileList",true)
                        && !header.equals("ProductCategoryList",true) && !header.equals("ClientStatusList",true)
                        && !header.equals("DPValueList",true) && !header.equals("AUMList",true)
                        && !header.equals("ActivityDate",true) && !header.equals("LeadAssignedDate",true)
                        && !header.equals("Status",true) && !header.equals("FirstTradedDate",true)
                        && !header.equals("LastMeetingDate",true) && !header.equals("CreatedBy",true)
                        && !header.equals("res",true) && !header.equals("AdvancedSearchID",true)
                        && !header.equals("Branch",true) && !header.equals("SubbrokerCode",true)
                        && !header.equals("AUM",true) && !header.equals("MTDRevenue",true)
                        && !header.equals("SearchTypeId",true) && !header.equals("ActivityTypeId",true)
                        && !header.equals("ProductName",true) && !header.equals("SavedSearchName",true)


                        )
                    {

                            colHeaders.add(com.mosl.mobile.domain.snapshot.ColHeader(header))

                    }
                }

                val headers = colHeaders.map {
                    it.copy(index = response.indexOf(it.label))
                }
                    .sortedBy { it.index }
                    .map { it.label }

                val data: ArrayList<AdvanceSearchData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    Log.d("json",""+Gson().toJson(json))
                    val values: ArrayList<Any> = ArrayList()
                    val hasMap: MutableMap<String, Any> = mutableMapOf<String, Any>()
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

                                    values.add(json.get(header))


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
                    data.add(AdvanceSearchData(setData(values), headers,hasMap))
                }
                return AdvanceSearchDataCombo(headers, data = data)
            } else {
                return  AdvanceSearchDataCombo(emptyList(), data = emptyList())
            }
        }
        else{
            return   AdvanceSearchDataCombo(emptyList(), data = emptyList())
        }
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