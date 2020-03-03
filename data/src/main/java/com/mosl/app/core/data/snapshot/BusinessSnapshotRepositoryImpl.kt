package com.mosl.app.core.data.snapshot

import com.mosl.mobile.domain.snapshot.BusinessSnapInput
import com.mosl.mobile.domain.snapshot.BusinessSnapshotRepository
import com.mosl.mobile.domain.snapshot.BusinessSnapshotResponseModel
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class BusinessSnapshotRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : BusinessSnapshotRepository {
  override fun getSnapshot(request: BusinessSnapInput): Observable<BusinessSnapshotResponseModel> {
    val leadRequest = request.requestTransform()
    return api.getBusinessSnapshot(leadRequest).map {
      it.responseTransform()
    }
  }
}