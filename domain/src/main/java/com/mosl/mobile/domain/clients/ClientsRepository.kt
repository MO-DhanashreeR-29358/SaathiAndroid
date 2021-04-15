package com.mosl.mobile.domain.clients

import io.reactivex.rxjava3.core.Observable


interface ClientsRepository {

    fun getClientsWidget(request: ClientInput): Observable<ClientItemResponseModel>

}