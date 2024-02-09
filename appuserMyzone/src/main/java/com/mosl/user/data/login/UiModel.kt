package com.mosl.user.data.login

/**
 * Created by snehaganapuram on 04-01-2018.
 */

data class UiModel<T>(
  val inProgress: Boolean,
  val data: T? = null,
  val error: Throwable? = null
) {
  val isSuccess: Boolean
    get() = data != null

  val isError: Boolean
    get() = error != null

  companion object {
    @JvmStatic
    fun <T> inProgress(): UiModel<T> {
      return UiModel(true, null, null)
    }

    @JvmStatic
    fun <T> success(data: T): UiModel<T> {
      return UiModel(false, data, null)
    }

    @JvmStatic
    fun <T> error(error: Throwable): UiModel<T> {
      return UiModel(false, null, error)
    }
  }
}
