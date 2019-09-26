package com.mosl.app.core.data.location

import com.mosl.mobile.domain.location.LocationDataModel
import com.mosl.mobile.domain.location.LocationRepository
import com.mosl.mobile.domain.location.LocationResponse
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(val saathiApi: SaathiApi) : LocationRepository {
  override fun getGeoLocation(locationList: List<LocationDataModel>): Observable<LocationResponse> {

    return saathiApi.saveGeoLocation(locationList.transformLocationData())
        .map {
          it.transform()
        }

  }

}