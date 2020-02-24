package com.mosl.mobile.domain.clients

import io.reactivex.Observable


interface ClientsRepository {

    fun getClientsWidget(request: ClientInput): Observable<ClientItemResponseModel>

}