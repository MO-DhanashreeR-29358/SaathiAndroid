package com.mosl.app.core.data.autarget

import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class TargetRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : TargetRepository {
    override fun getTargetWidget(request: TargetInput): Observable<TargetItemModel> {
        val targetRequest = request.requestTransform()
        return api.getTargetWidgetData(targetRequest).map {

            it.responseTransform()
        }
    }

}