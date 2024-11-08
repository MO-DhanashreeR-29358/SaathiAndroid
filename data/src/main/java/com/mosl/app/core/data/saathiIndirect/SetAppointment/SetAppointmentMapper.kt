package com.mosl.app.core.data.saathiIndirect.SetAppointment

import com.mosl.app.core.data.util.addDefaultSelection
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.ResponseItem

object SetAppointmentMapper {

    fun transformLookupData(
        list: List<ResponseItem?>,
        type: String
    ): List<LookUp> {
        return list.map { LookUp(it?.lookUpid?:0, it?.lookUpName ?: "") }
            .addDefaultSelection(LookUp(0, type))
    }

}