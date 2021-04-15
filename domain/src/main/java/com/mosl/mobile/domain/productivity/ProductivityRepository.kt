package com.mosl.mobile.domain.productivity

import io.reactivex.rxjava3.core.Observable

interface ProductivityRepository {
  fun getDashboardData(request: ProductivityInput): Observable<ProductivityCombo>
}