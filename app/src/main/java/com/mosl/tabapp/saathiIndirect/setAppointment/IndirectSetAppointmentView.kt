package com.mosl.tabapp.saathiIndirect.setAppointment

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import io.reactivex.rxjava3.core.Observable

interface IndirectSetAppointmentView :BaseView {

    fun fnRenderActivityType(uiModel: UiModel<List<LookUp>>)
    fun onActivityTypeSelected(): Observable<Int>
    fun getSelectedActivityType(): LookUp
    fun fnRenderMeetingReason(uiModel: UiModel<List<LookUp>>)
    fun renderDatePicker()
    fun onDatePicker(): Observable<Unit>

}