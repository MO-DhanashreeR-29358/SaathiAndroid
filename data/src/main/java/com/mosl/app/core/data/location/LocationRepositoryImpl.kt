package com.mosl.app.core.data.location

import com.mosl.mobile.domain.location.LocationDataModel
import com.mosl.mobile.domain.location.LocationRepository
import com.mosl.mobile.domain.location.LocationResponse
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.location.LocationRequest
import io.reactivex.Observable
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(val saathiApi: SaathiApi) : LocationRepository {
  override fun getGeoLocation(locationList: List<LocationDataModel>): Observable<LocationResponse> {

    val listMapper = locationList.transformLocationData()
    val request = LocationRequest(listMapper)

    return saathiApi.saveGeoLocation(request)
      .map {
        it.transform()
      }

  }

}