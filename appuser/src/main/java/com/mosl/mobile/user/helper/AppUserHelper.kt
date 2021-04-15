package com.mosl.mobile.user.helper

import android.content.Context
import android.content.SharedPreferences
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import io.reactivex.rxjava3.core.ObservableTransformer

fun Context.getPref(name: String): SharedPreferences {
  return getSharedPreferences(name, Context.MODE_PRIVATE)
}

fun <T> SaathiCommonResponse<T>.getData(): T {
  if (isSuccessful) {
    return response!!
  }
  throw ServiceFailureException(getMessage())
}

internal fun <T> applyUiModel(): ObservableTransformer<T, UiModel<T>> {
  return ObservableTransformer { observable ->
    observable.map { UiModel.success(it) }
        .onErrorReturn { UiModel.error(it) }
  }
}
