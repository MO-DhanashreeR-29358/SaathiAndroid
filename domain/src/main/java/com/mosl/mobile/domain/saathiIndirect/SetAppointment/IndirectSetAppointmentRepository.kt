package com.mosl.mobile.domain.saathiIndirect.SetAppointment

import com.mosl.mobile.domain.createmeeting.LookUp
import io.reactivex.rxjava3.core.Observable

interface IndirectSetAppointmentRepository {

    fun getActivitytype(): Observable<List<LookUp>>

    fun getMeetingReason(intParnetId:Int):Observable<List<LookUp>>




}