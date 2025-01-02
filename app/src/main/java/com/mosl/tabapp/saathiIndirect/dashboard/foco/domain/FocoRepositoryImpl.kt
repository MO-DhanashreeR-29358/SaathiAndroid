package com.mosl.tabapp.saathiIndirect.dashboard.foco.domain

import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FocoRepositoryImpl @Inject constructor(
    val api: SaathiIndirectApi
) : FocoRepository {


    override fun getPeriodFilter(request: FocoPeriodFilterInput): Observable<List<FocoPeriodModel>> {
        val targetRequest = request.requestTransform()
        return api.getFocoPeriodFilter(targetRequest).map {
                it.data.list.itemTransform()
        }
    }

}