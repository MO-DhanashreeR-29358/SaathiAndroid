package com.mosl.mobile.domain.location

import io.reactivex.Observable

interface LocationRepository {

  fun getGeoLocation(locationList: List<LocationDataModel>): Observable<LocationResponse>
}