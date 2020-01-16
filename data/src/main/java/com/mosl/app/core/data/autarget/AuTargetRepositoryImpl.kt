package com.mosl.app.core.data.autarget

import com.mosl.mobile.domain.autarget.AuTargetInput
import com.mosl.mobile.domain.autarget.AuTargetRepository
import com.mosl.mobile.domain.autarget.TargetItemModel
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class AuTargetRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : AuTargetRepository {
    override fun getAuTargetWidget(request: AuTargetInput): Observable<TargetItemModel> {
        val auTargetRequest = request.requestTransform()
        return api.getAuTargetWidgetData(auTargetRequest).map {

            it.responseTransform()
        }
    }

}