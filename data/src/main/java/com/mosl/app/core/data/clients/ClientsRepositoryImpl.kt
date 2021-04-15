package com.mosl.app.core.data.clients

import com.mosl.mobile.domain.clients.ClientInput
import com.mosl.mobile.domain.clients.ClientItemResponseModel
import com.mosl.mobile.domain.clients.ClientsRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ClientsRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : ClientsRepository {
    override fun getClientsWidget(request: ClientInput): Observable<ClientItemResponseModel> {
        val leadRequest = request.requestTransform()
        return api.getClientsWidgetData(leadRequest).map {
            it.responseTransform()
        }
    }

}