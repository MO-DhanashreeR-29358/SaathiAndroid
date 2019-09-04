@file:JvmName("UiModelHelper")

package com.mosl.app.core.data

import com.mosl.app.core.data.UiModel.Fail
import com.mosl.app.core.data.UiModel.Success
import com.mosl.app.core.rx.Dispatchers
import com.mosl.app.core.rx.RxDispatchers
import io.reactivex.ObservableTransformer

fun <T> applyUiModel(): ObservableTransformer<T, UiModel<T>> {
  return RxDispatchers.applyUiModel()
}

fun <T> applyRvUiModel(): ObservableTransformer<T, UiModel<T>> {
  return RxDispatchers.applyRvUiModel()
}

fun <T> Dispatchers.applyUiModel(): ObservableTransformer<T, UiModel<T>> {
  return ObservableTransformer { observable ->
    observable.map { UiModel.success(it) }
        .onErrorReturn { UiModel.error(it) }
        .observeOn(main)
        .startWith(UiModel.InProgress)
  }
}

fun <T> Dispatchers.applyRvUiModel(): ObservableTransformer<T, UiModel<T>> {
  return ObservableTransformer { observable ->
    observable.map { UiModel.success(it) }
        .onErrorReturn { UiModel.error(it) }
        .startWith(UiModel.InProgress)
        .observeOn(main)
  }
}

inline fun <T> UiModel<T>.onSuccess(action: (data: T) -> Unit) {
  if (this is Success) {
    action(this.value)
  }
}

inline fun UiModel<*>.onFailure(action: (error: Throwable) -> Unit) {
  if (this is Fail) {
    action(this.throwable)
  }
}
