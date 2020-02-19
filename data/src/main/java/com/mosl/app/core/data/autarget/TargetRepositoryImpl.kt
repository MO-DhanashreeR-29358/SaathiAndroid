package com.mosl.app.core.data.autarget

import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.mobile.domain.target.TargetRequest
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class TargetRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : TargetRepository {
    override fun getAuTargetWidget(request: TargetRequest): Observable<TargetItemModel> {
        val auTargetRequest = request.requestTransform()
        return api.getAuTargetWidgetData(auTargetRequest).map {

            it.responseTransform()
        }
    }

}