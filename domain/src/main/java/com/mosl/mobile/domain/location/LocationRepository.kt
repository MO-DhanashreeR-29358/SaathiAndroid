package com.mosl.mobile.domain.location

import io.reactivex.rxjava3.core.Observable

interface LocationRepository {

  fun getGeoLocation(locationList: List<LocationDataModel>): Observable<LocationResponse>
}