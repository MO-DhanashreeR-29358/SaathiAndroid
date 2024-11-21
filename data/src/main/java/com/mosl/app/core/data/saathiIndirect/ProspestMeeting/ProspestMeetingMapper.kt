package com.mosl.app.core.data.saathiIndirect.ProspestMeeting


import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ResponseItem
import io.reactivex.rxjava3.core.Observable

object ProspestMeetingMapper {
    fun transformListData(
        list: List<ResponseItem?>,
        type: String
    ):List<ClientSearchModel> {
        return list.map {ClientSearchModel(franchiseeCode = it?.franchiseeCode?:"", leadId = it?.leadId?:0, clientCODE = it?.clientCODE?:"")  }

    }
}