package com.mosl.app.core.data

import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.saathi.api.model.attendance.HrmsErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class HRMSResponseWrapper @Inject constructor() : Converter.Factory() {

  override fun responseBodyConverter(
    type: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *>? {
    if (type !is Class<*>) {
      return null
    }
    if (!HrmsErrorResponse::class.java.isAssignableFrom(type)) {
      return null
    }
    val responseBody =
      retrofit.nextResponseBodyConverter<HrmsErrorResponse>(this, type, annotations)
    return Converter { value: ResponseBody ->
      val response = responseBody.convert(value)

      if (response == null || response.hasError()) {
        throw ServiceFailureException(response?.errorMessage ?: "Something went wrong")
      }

      return@Converter response
    }
  }
}