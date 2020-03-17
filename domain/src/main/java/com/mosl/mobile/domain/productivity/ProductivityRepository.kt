package com.mosl.mobile.domain.productivity

import io.reactivex.Observable

interface ProductivityRepository {
  fun getDashboardData(request: ProductivityInput): Observable<ProductivityCombo>
}