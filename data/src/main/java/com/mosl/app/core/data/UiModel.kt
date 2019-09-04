package com.mosl.app.core.data

/**
 * Created by snehaganapuram on 04-01-2018.
 */

sealed class UiModel<out T> constructor(
    @JvmField val inProgress: Boolean,
    @JvmField val data: T? = null,
    @JvmField val error: Throwable? = null
) {
  val isSuccess: Boolean
    get() = data != null

  val isError: Boolean
    get() = error != null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as UiModel<*>

    if (inProgress != other.inProgress) return false
    if (data != other.data) return false
    if (error != other.error) return false

    return true
  }

  override fun hashCode(): Int {
    var result = inProgress.hashCode()
    result = 31 * result + (data?.hashCode() ?: 0)
    result = 31 * result + (error?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "UiModel(inProgress=$inProgress, data=$data, error=$error)"
  }

  object InProgress : UiModel<Nothing>(true)

  data class Success<out T>(
      @JvmField val value: T
  ) : UiModel<T>(false, data = value)

  data class Fail<out T>(
      @JvmField val throwable: Throwable
  ) : UiModel<T>(false, error = throwable)

  companion object {
    @JvmStatic
    fun <T> inProgress(): UiModel<T> {
      return InProgress
    }

    @JvmStatic
    fun <T> success(data: T): UiModel<T> {
      return Success(data)
    }

    @JvmStatic
    fun <T> error(error: Throwable): UiModel<T> {
      return Fail(throwable = error)
    }
  }
}
