package com.mosl.app.core.data

import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.saathi.api.model.attendance.HrmsErrorResponse
import com.mosl.saathi.api.model.common.ExtraDataCommonResponse
import com.mosl.saathi.api.model.common.ExtraDataWrapper
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
    if (MOSLResponseWrapper.hasExtraDataWrapper(annotations)) {
      val envelopedType = TypeToken.getParameterized(
        ExtraDataCommonResponse::class.java, type
      ).type
      val delegate = retrofit.nextResponseBodyConverter<ExtraDataCommonResponse<*>>(
        this,
        envelopedType,
        annotations
      )
      return Converter { value: ResponseBody ->
        val baseResponse = delegate.convert(value)
        val code = "ErrorOccurred"
        val message = "ErrorOccurred"
        if (baseResponse != null) {
          val data = baseResponse.data
          if (data != null) {
            return@Converter data
          }
        }
        throw ServiceFailureException(code, message)
      }
    }else{
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
}

private fun hasExtraDataWrapper(annotations: Array<Annotation>): Boolean {
  for (annotation in annotations) {
    if (annotation.annotationClass == ExtraDataWrapper::class.java) {
      return true
    }
  }
  return false
}